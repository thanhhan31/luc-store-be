package com.lucistore.lucistorebe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductImageDTO;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductImage;
import com.lucistore.lucistorebe.repo.ProductImageRepo;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;

@Service
public class ProductImageService {
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ProductImageRepo productImageRepo;
	
	@Autowired
	MediaResourceService mediaResourceService;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	@Transactional
	public ListResponse<ProductImageDTO> create(Long idProduct, List<MultipartFile> images) {
		return create(productRepo.getReferenceById(idProduct), images);
	}
	
	@Transactional
	public ListResponse<ProductImageDTO> create(Product p, List<MultipartFile> images) {
		List<MediaResource> mrs = new ArrayList<>();
		List<ProductImage> pimgs = new ArrayList<>();
		
		try {
			for (MultipartFile image : images) {
				MediaResource mr = mediaResourceService.save(image.getBytes());
				mrs.add(mr);
				pimgs.add(
						productImageRepo.save(
							new ProductImage(
									p, 
									mr
								)
						)
					);
			}
		} catch (IOException e) {
			for (MediaResource mr : mrs) {
				mediaResourceService.delete(mr);
			}
			throw new CommonRuntimeException("Error occurred when trying to save product image");
		}
		
		return serviceUtils.convertToListResponse(pimgs, ProductImageDTO.class);
	}
	
	public void delete(Long idProduct, List<Long> idProductImages) {
		if (idProductImages.isEmpty()) {
			throw new CommonRuntimeException("At least one image is specified to be deleted");
		}
		
		List<ProductImage> l = new ArrayList<>();
		for (Long idProductImage : idProductImages) {
			ProductImage img = productImageRepo.getReferenceById(idProductImage);
			Product p = img.getProduct();
			
			if (!p.getId().equals(idProduct)) {
				throw new CommonRuntimeException("Product doesn't have any image with id " + idProductImage.toString());
			}
			
			l.add(img);
		}
		deleteWithEntity(l);
	}
	
	@Transactional
	private void deleteWithEntity(List<ProductImage> productImages) {
		if (productImages.get(0).getProduct().getImages().size() - productImages.size() < PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_IMAGE) {
			throw new InvalidInputDataException(String.format("Product should have at least %d image(s)", PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_IMAGE));
		}
		
		for (ProductImage productImage : productImages) {
			MediaResource media = productImage.getMedia();
			productImage.setMedia(null);
			mediaResourceService.delete(media.getId());
			productImageRepo.delete(productImage);
		}
	}
}
