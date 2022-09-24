package com.lucistore.lucistorebe.controller.payload.request.productcategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProductCategoryRequest {
	@NotNull
	private Long id;
	
	@Size(min = 2, message = "Product category name should have at least 2 characters")
	private String name;
	
	private Long idParent;
}
