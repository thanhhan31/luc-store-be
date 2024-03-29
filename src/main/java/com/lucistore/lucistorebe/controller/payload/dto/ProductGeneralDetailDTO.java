package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.utility.EProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductGeneralDetailDTO {
	private Long id;
	private ProductCategoryGeneralDTO category;
	
	private String name;
	private String avatar;
	private String description;

	private Long minPrice;
	private Long maxPrice;
	private Integer maxDiscount;
	private Long nsold;
	private Long nvisit;
	private EProductStatus status;
	
	private Double averageRating;
}
