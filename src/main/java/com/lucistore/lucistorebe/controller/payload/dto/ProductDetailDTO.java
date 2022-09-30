package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.List;

import com.lucistore.lucistorebe.utility.EProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductDetailDTO {
	private Long id;
	private ProductCategoryDTO category;
	
	private String name;
	private String avatar;
	private String description;

	private Long minPrice;
	private Long maxPrice;
	private Long nsold;
	private Long nvisit;
	private EProductStatus status;

	private List<ProductImageDTO> images;
	private List<ProductVariationDTO> variations;
}
