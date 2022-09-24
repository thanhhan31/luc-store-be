package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductCategoryDTO {
	private Long id;
	private Long idParent;
	private String name;
	private List<ProductChildCategoryDTO> child;
}
