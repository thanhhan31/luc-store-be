package com.lucistore.lucistorebe.controller.payload.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductReviewImageDTO {
	private Long id;
	private String url;
	private String resourceType;
}
