package com.lucistore.lucistorebe.controller.payload.dto.productdetail;

import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.ProductVariationDTO;
import com.lucistore.lucistorebe.utility.EProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductCartDetailDTO {
	private Long id;
	private ProductCategoryDetailDTO category;
	
	private String name;
	private String avatar;
	private String description;

	private Long minPrice;
	private Long maxPrice;

	private EProductStatus status;

	private List<ProductVariationDTO> variations;
}
