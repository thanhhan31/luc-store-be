package com.lucistore.lucistorebe.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRestException;
import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductDetailDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductGeneralDetailDTO;
import com.lucistore.lucistorebe.controller.payload.request.product.CreateProductRequest;
import com.lucistore.lucistorebe.controller.payload.request.product.UpdateProductRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.repo.ProductCategoryRepo;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.Page;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;
import com.lucistore.lucistorebe.utility.ServiceDataReturnConverter;

@Service
public class ProductService {
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
	ServiceDataReturnConverter returnConverter;
	
	public ListWithPagingResponse<ProductGeneralDetailDTO> search(Long idCategory, String searchName, String searchDescription, 
			EProductStatus status, Long minPrice, Long maxPrice, Integer currentPage, Integer size, Sort sort) {
		int count = productRepo.searchCount(idCategory, searchName, searchDescription, status, minPrice, maxPrice).intValue();
		Page page = new Page(currentPage, size, count, sort);
		
		return returnConverter.convertToListResponse(
				productRepo.search(idCategory, searchName, searchDescription, status, minPrice, maxPrice, page),
				ProductGeneralDetailDTO.class, 
				page
			);
	}
	
	public DataResponse<ProductGeneralDetailDTO> getById(Long id) {
		return returnConverter.convertToDataResponse(
				productRepo.findById(id).orElseThrow(() -> new InvalidInputDataException("No product found with given id")),
				ProductGeneralDetailDTO.class
			);
	}
	
	@Transactional
	public DataResponse<ProductDetailDTO> create(CreateProductRequest data, MultipartFile avatar, List<MultipartFile> images) {
		if (!productCategoryRepo.existsById(data.getIdCategory()))
			throw new InvalidInputDataException("No category found with given id");
		
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
					mediaResourceService.save(avatar.getBytes())
				);
		} catch (IOException e) {
			throw new CommonRuntimeException("Error occurred when trying to save product avatar image");
		}
		
		p = productRepo.saveAndFlush(p);
		productImageService.create(p, images);
		productVariationService.create(p, data.getVariations());
		productRepo.refresh(p);
		
		return returnConverter.convertToDataResponse(p, ProductDetailDTO.class);
	}
	
	@Transactional
	public DataResponse<ProductDetailDTO> update(Long id, UpdateProductRequest data, MultipartFile avatar) {
		Product p = productRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No product found with given id")
			);
		
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
		
		if (avatar != null) {
			byte[] newAvatar;
			try {
				newAvatar = avatar.getBytes();
			} catch (IOException e) {
				throw new CommonRestException("Can not processing new product avatar");
			}
			
			if (p.getAvatar() != null) {
				var oldAvatar = p.getAvatar();
				p.setAvatar(null);
				mediaResourceService.delete(oldAvatar.getId());
			}
			p.setAvatar(mediaResourceService.save(newAvatar));
		}
		
		return returnConverter.convertToDataResponse(productRepo.save(p), ProductDetailDTO.class);
	}
}
