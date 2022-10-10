package com.lucistore.lucistorebe.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductCategoryDTO;
import com.lucistore.lucistorebe.controller.payload.request.productcategory.CreateProductCategoryRequest;
import com.lucistore.lucistorebe.controller.payload.request.productcategory.UpdateProductCategoryRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.repo.ProductCategoryRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EProductCategoryStatus;

@Service
public class ProductCategoryService {
	@Autowired 
	ProductCategoryRepo productCategoryRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<ProductCategoryDTO> get(Long id) {
		ProductCategory category = productCategoryRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No product category found with given id")
			);
		
		return serviceUtils.convertToDataResponse(category, ProductCategoryDTO.class);
		
	}
	
	public ListResponse<ProductCategoryDTO> getAllRootCategories() {
		return serviceUtils.convertToListResponse(productCategoryRepo.findAllRootCategories(), ProductCategoryDTO.class);
	}
	
	public DataResponse<ProductCategoryDTO> create(CreateProductCategoryRequest data) {
		ProductCategory category;
		
		if (data.getIdParent() != null) {
			ProductCategory parent = productCategoryRepo.findById(data.getIdParent()).orElseThrow(
					() -> new InvalidInputDataException("Invalid parent product category id")
				);
			category = new ProductCategory(parent, data.getName());
		}
		else {
			category = new ProductCategory(data.getName());
		}
		
		category.setStatus(EProductCategoryStatus.ACTIVE);
		
		return serviceUtils.convertToDataResponse(productCategoryRepo.save(category), ProductCategoryDTO.class);
	}
	
	public DataResponse<ProductCategoryDTO> update(Long id, UpdateProductCategoryRequest data) {
		ProductCategory category = productCategoryRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No product category found with given id")
			);
		
		if (data.getIdParent() != null && !category.getParent().getId().equals(data.getIdParent())) {
			if (productCategoryRepo.existsById(data.getIdParent())) {
				throw new InvalidInputDataException("Invalid parent product category id");
			}
			else {
				category.setParent(productCategoryRepo.getReferenceById(data.getIdParent()));
			}
		}
		
		if (StringUtils.isNotBlank(data.getName()) && !category.getName().equals(data.getName())) {
			category.setName(data.getName());
		}
		
		if (data.getStatus() != null && !category.getStatus().equals(data.getStatus())) {
			category.setStatus(data.getStatus());
		}
		
		return serviceUtils.convertToDataResponse(productCategoryRepo.save(category), ProductCategoryDTO.class);
	}
}
