package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.utility.EProductVariationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductVariationDTO {
	private Long id;
	private Long idProduct;
	private String name;
	private Long price;
	private Long availableQuantity;
	private Integer discount;
	private EProductVariationStatus status;
}
