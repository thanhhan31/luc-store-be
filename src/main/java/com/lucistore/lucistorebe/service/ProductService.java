package com.lucistore.lucistorebe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductGeneralDetailDTO;
import com.lucistore.lucistorebe.controller.payload.dto.productdetail.ProductDetailDTO;
import com.lucistore.lucistorebe.controller.payload.request.product.CreateProductRequest;
import com.lucistore.lucistorebe.controller.payload.request.product.UpdateProductRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.entity.product.ProductVariation;
import com.lucistore.lucistorebe.repo.ProductCategoryRepo;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EProductCategoryStatus;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.EProductVariationStatus;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;

@Service
public class ProductService {
	@Autowired
	LogService logService;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ProductImageService productImageService;
	
	@Autowired
	MediaResourceService mediaResourceService;
	
	@Autowired
	ProductVariationService productVariationService;
	
	@Autowired
	ProductCategoryRepo productCategoryRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public ListWithPagingResponse<ProductGeneralDetailDTO> search(Long idCategory, String searchName, String searchDescription, 
			EProductStatus status, Long minPrice, Long maxPrice, Integer currentPage, Integer size, Sort sort, boolean isBuyer) {
		
		List<Long> idsCategory = new ArrayList<>();
		
		if (idCategory != null) {
			var pc = productCategoryRepo.findById(idCategory).orElse(null);
			if (pc != null) {
				
				if (isBuyer) {
					if (serviceUtils.checkStatusProductCategory(pc, EProductCategoryStatus.BANNED))
						throw new InvalidInputDataException("Given category id has been banned");
					else {
						if (!pc.getChild().isEmpty()) {
							for (var c : pc.getChild()) {
								if (c.getStatus() != EProductCategoryStatus.BANNED)
									idsCategory.add(c.getId());
							}
						}
					}
				}
				else {
					idsCategory.addAll(pc.getChild().stream().map(ProductCategory::getId).toList());
				}
				
				idsCategory.add(idCategory);
			}
			else {
				throw new InvalidInputDataException("Given category id does not exists");
			}
		}
		
		int count = productRepo.searchCount(idsCategory, searchName, searchDescription, status, minPrice, maxPrice).intValue();
		PageWithJpaSort page = new PageWithJpaSort(currentPage, size, count, sort);
		
		return serviceUtils.convertToListResponse(
				productRepo.search(idsCategory, searchName, searchDescription, status, minPrice, maxPrice, page),
				ProductGeneralDetailDTO.class, 
				page
			);
	}
	
	public DataResponse<ProductDetailDTO> getById(Long id, boolean isBuyer) {
		Product p = productRepo.findById(id).orElseThrow(() -> new InvalidInputDataException("No product found with given id"));
		
		if (isBuyer && 
				(p.getStatus() == EProductStatus.DISABLED || serviceUtils.checkStatusProductCategory(p.getCategory(), EProductCategoryStatus.BANNED)))
			throw new InvalidInputDataException("No product found with given id");
		else {
			if (isBuyer)
				productRepo.updateVisitCount(id);
			
			if (p.getCategory().getParent() != null)
				p.setParents(productCategoryRepo.findAncestry(p.getCategory().getParent().getId()));
		}
		
		return serviceUtils.convertToDataResponse(
				p,
				ProductDetailDTO.class
			);
	}
	
	@Transactional
	public DataResponse<ProductDetailDTO> create(Long idUser, CreateProductRequest data, MultipartFile avatar, List<MultipartFile> images) {
		var productCategory = productCategoryRepo.findById(data.getIdCategory())
				.orElseThrow(() -> new InvalidInputDataException("No category found with given id"));
		
		if (!serviceUtils.checkStatusProductCategory(productCategory, EProductCategoryStatus.ACTIVE))
			throw new InvalidInputDataException("Can not create new product to this category or its parent category because it has been disabled");
		
		if (data.getVariations().size() < PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_VARIATION) {
			throw new InvalidInputDataException(
					String.format(
							"At least %d product variation(s) is required", 
							PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_VARIATION
					)
				);
		}
		
		Product p;
		try {
			p = new Product(
					productCategoryRepo.getReferenceById(data.getIdCategory()), 
					data.getName(), 
					data.getDescription(),
					mediaResourceService.save(avatar.getBytes()),
					EProductStatus.DISABLED
				);
		} catch (IOException e) {
			throw new CommonRuntimeException("Error occurred when trying to save product avatar image");
		}
		
		p = productRepo.saveAndFlush(p);
		productImageService.create(p, images);
		productVariationService.create(p, data.getVariations());
		productRepo.refresh(p);
		updatePrice(p, p.getVariations());
		
		if (p.getCategory().getParent() != null) {
			p.setParents(productCategoryRepo.findAncestry(p.getCategory().getParent().getId()));
		}		
		
		logService.logInfo(idUser, String.format("New product has been created with id %d", p.getId()));
		
		return serviceUtils.convertToDataResponse(p, ProductDetailDTO.class);
	}
	
	@Transactional
	public DataResponse<ProductDetailDTO> update(Long idUser, Long id, UpdateProductRequest data, MultipartFile avatar) {
		Product p = productRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No product found with given id")
			);
		
		if (data != null) {
			if (StringUtils.isNotEmpty(data.getName()) && !p.getName().equals(data.getName()))
				p.setName(data.getName());
			
			if (StringUtils.isNotEmpty(data.getDescription()) && !p.getDescription().equals(data.getDescription()))
				p.setDescription(data.getDescription());
			
			if (data.getIdCategory() != null && !p.getCategory().getId().equals(data.getIdCategory())) {
				if (!productCategoryRepo.existsById(data.getIdCategory()))
					throw new InvalidInputDataException("No category found with given id");
				p.setCategory(productCategoryRepo.getReferenceById(data.getIdCategory()));
			}
			
			if (data.getStatus() != null && !p.getStatus().equals(data.getStatus()))
				p.setStatus(data.getStatus());
		}
		
		if (avatar != null) {
			serviceUtils.updateAvatar(p, avatar);
		}
		
		p = productRepo.save(p);
		p.setParents(productCategoryRepo.findAncestry(p.getCategory().getParent().getId()));
		
		logService.logInfo(idUser, String.format("Product with id %d has been edited", p.getId()));
		
		return serviceUtils.convertToDataResponse(p, ProductDetailDTO.class);
	}
	
	/***
	 * For entity listener
	 * @param pv
	 */
	public void updatePriceWhenVariationCreated(ProductVariation pv) {
		Product p = pv.getProduct();
		List<ProductVariation> pvs = new ArrayList<>(p.getVariations());
		pvs.add(pv);
		updatePrice(p, pvs);
	}
	
	/***
	 * For entity listener
	 * @param pv
	 */
	public void updatePriceWhenVariationChange(ProductVariation pv) {
		Product p = pv.getProduct();
		List<ProductVariation> pvs = new ArrayList<>(p.getVariations());
		updatePrice(p, pvs);
	}
	
	private void updatePrice(Product p, List<ProductVariation> pvs) {
		Supplier<Stream<ProductVariation>> streamSupplier = () -> pvs
				.stream()
				.filter(x -> x.getAvailableQuantity() > 0 && x.getStatus() == EProductVariationStatus.ENABLED);
		
		Optional<ProductVariation> minPv = streamSupplier.get().min(
				(first, second) -> Long.compare(first.getPriceAfterDiscount(), second.getPriceAfterDiscount())
			);
		Optional<ProductVariation> maxPv = streamSupplier.get().max(
				(first, second) -> Long.compare(first.getPriceAfterDiscount(), second.getPriceAfterDiscount())
			);

		if (minPv.isPresent())
			p.setMinPrice(minPv.get().getPriceAfterDiscount());
		if (maxPv.isPresent())
			p.setMaxPrice(maxPv.get().getPriceAfterDiscount());
		
		productRepo.save(p);
	}
}
