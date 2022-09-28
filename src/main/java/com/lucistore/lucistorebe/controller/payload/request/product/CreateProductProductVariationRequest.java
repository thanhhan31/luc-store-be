package com.lucistore.lucistorebe.controller.payload.request.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateProductProductVariationRequest {
	@NotBlank
	private String variationName;
	
	@NotNull
	@Range(min = 1)
	private Long price;
	
	@NotNull
	private Long availableQuantity;
	
	private Double discount;
}