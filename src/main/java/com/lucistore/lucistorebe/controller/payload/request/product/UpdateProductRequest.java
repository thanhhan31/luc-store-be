package com.lucistore.lucistorebe.controller.payload.request.product;

import com.lucistore.lucistorebe.utility.EProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdateProductRequest {

	private String name;
	
	private String description;

	private Long idCategory;
	
	private EProductStatus status;
}
