package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.utility.EProductCategoryStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductCategoryGeneralDTO {
	private Long id;	
	private String name;
	private Integer level;
	private EProductCategoryStatus status;
}
