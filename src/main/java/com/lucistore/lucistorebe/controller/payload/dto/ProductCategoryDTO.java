package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.List;

import com.lucistore.lucistorebe.utility.EProductCategoryStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductCategoryDTO {
	private Long id;
	private Long idParent;
	private String name;
	private EProductCategoryStatus status;
	private List<ProductCategoryGeneralDTO> child;
}
