package com.lucistore.lucistorebe.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRestException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductVariationDTO;
import com.lucistore.lucistorebe.controller.payload.request.product.CreateProductVariationRequest;
import com.lucistore.lucistorebe.controller.payload.request.product.UpdateProductVariationRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductVariation;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EProductVariationStatus;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;

@Service
public class ProductVariationService {
	@Autowired
	LogService logService;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ProductVariationRepo productVariationRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<ProductVariationDTO> getById(Long id) {
		ProductVariation variation = productVariationRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No product category found with given id")
			);
		return serviceUtils.convertToDataResponse(variation, ProductVariationDTO.class);
	}
	
	public DataResponse<ProductVariationDTO> create(Long idUser, Long idProduct, CreateProductVariationRequest data) {
		if (!productRepo.existsById(idProduct))
			throw new InvalidInputDataException("No product found with given id");
		
		ProductVariation variation = new ProductVariation(
				productRepo.getReferenceById(idProduct), 
				data.getVariationName(),
				data.getTier(),
				data.getPrice(), 
				data.getAvailableQuantity(), 
				data.getDiscount(), 
				EProductVariationStatus.ENABLED
			);
		
		variation = productVariationRepo.saveAndFlush(variation);
		
		logService.logInfo(idUser, 
				String.format("New product variation for product (id: %d) has been created with id %d", idProduct, variation.getId()));
		
		return serviceUtils.convertToDataResponse(variation, ProductVariationDTO.class);
	}
	
	public void create(Product p, List<CreateProductVariationRequest> data) {
		for (CreateProductVariationRequest variation : data) {
			productVariationRepo.save(
					new ProductVariation(
						p, 
						variation.getVariationName(), 
						variation.getTier(),
						variation.getPrice(), 
						variation.getAvailableQuantity(), 
						variation.getDiscount(), 
						EProductVariationStatus.ENABLED
					)
				);
		}
	}
	
	public DataResponse<ProductVariationDTO> update(Long idUser, Long id, Long idProduct, UpdateProductVariationRequest data) {		
		ProductVariation variation = productVariationRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No product variation found with given id")
			);
		
		if (!variation.getProduct().getId().equals(idProduct)) {
			throw new InvalidInputDataException("Wrong product variation with given product");
		}
		
		if (StringUtils.isNotEmpty(data.getVariationName()) && !variation.getVariationName().equals(data.getVariationName())) {
			variation.setVariationName(data.getVariationName());
		}
		
		if (StringUtils.isNotEmpty(data.getTier()) && !variation.getTier().equals(data.getTier())) {
			variation.setTier(data.getTier());
		}
		
		if (data.getPrice() != null && !variation.getPrice().equals(data.getPrice())) {
			variation.setPrice(data.getPrice());
		}
		
		if (data.getAvailableQuantity() != null && !variation.getAvailableQuantity().equals(data.getAvailableQuantity())) {
			variation.setAvailableQuantity(data.getAvailableQuantity());
		}
		
		if (data.getDiscount() != null && !variation.getDiscount().equals(data.getDiscount())) {
			variation.setDiscount(data.getDiscount());
		}
		
		if (data.getStatus() != null && !variation.getStatus().equals(data.getStatus())) {
			if (data.getStatus() == EProductVariationStatus.DISABLED && 
					variation.getProduct().getVariations().size() <= PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_VARIATION) {
				throw new CommonRestException(
						String.format(
							"A product must have at least %s enabled product(s) variation", 
							PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_VARIATION
						)
					);
			}
			variation.setStatus(data.getStatus());
		}
		
		logService.logInfo(idUser, 
				String.format("Product variation with id %d for product (id: %d) has been edited", variation.getId(), idProduct));
		
		return serviceUtils.convertToDataResponse(productVariationRepo.save(variation), ProductVariationDTO.class);
	}
}
