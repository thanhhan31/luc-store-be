package com.lucistore.lucistorebe.controller.payload.request.product;

import org.hibernate.validator.constraints.Range;

import com.lucistore.lucistorebe.utility.EProductVariationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProductVariationRequest {	
	
	private String variationName;
	
	private String tier;
	
	@Range(min = 1)
	private Long price;
	
	@Range(min = 0)
	private Long availableQuantity;
	
	@Range(min = 0, max = 100, message = "Allowed discount value is from 0 to 100")
	private Integer discount;
	
	private EProductVariationStatus status;
}