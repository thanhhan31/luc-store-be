package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductVariationDTO;
import com.lucistore.lucistorebe.controller.payload.request.product.CreateProductVariationRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductVariation;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.utility.EProductVariationStatus;
import com.lucistore.lucistorebe.utility.ServiceDataReturnConverter;

@Service
public class ProductVariationService {
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ProductVariationRepo productVariationRepo;
	
	@Autowired
	ServiceDataReturnConverter returnConverter;
	
	public DataResponse<ProductVariationDTO> getById(Long id) {
		ProductVariation variation = productVariationRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No product category found with given id")
			);
		return returnConverter.convertToDataResponse(variation, ProductVariationDTO.class);
	}
	
	public DataResponse<ProductVariationDTO> create(Long idProduct, CreateProductVariationRequest data) {
		if (!productRepo.existsById(idProduct))
			throw new InvalidInputDataException("No product found with given id");
		
		ProductVariation variation = new ProductVariation(
				productRepo.getReferenceById(idProduct), 
				data.getVariationName(), 
				data.getPrice(), 
				data.getAvailableQuantity(), 
				data.getDiscount(), 
				EProductVariationStatus.ENABLED
			);
		
		return returnConverter.convertToDataResponse(productVariationRepo.save(variation), ProductVariationDTO.class);
	}
}
