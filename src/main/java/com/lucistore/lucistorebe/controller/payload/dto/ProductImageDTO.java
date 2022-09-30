package com.lucistore.lucistorebe.controller.payload.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductImageDTO {
	private Long id;
	private Long idProduct;
	private String url;
	private String resourceType;
}
