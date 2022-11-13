package com.lucistore.lucistorebe.controller.payload.dto.productdetail;

import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.ProductImageDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductVariationDTO;
import com.lucistore.lucistorebe.utility.EProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductDetailDTO {
	private Long id;
	private ProductCategoryDetailDTO category;
	
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
	private Integer rating1;
	private Integer rating2;
	private Integer rating3;
	private Integer rating4;
	private Integer rating5;
	private Integer totalRatingTimes;
	
	private List<String> tierVariations;
	private List<ProductImageDTO> images;
	private List<ProductVariationDTO> variations;
}
