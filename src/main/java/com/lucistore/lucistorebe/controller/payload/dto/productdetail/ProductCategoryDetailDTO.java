package com.lucistore.lucistorebe.controller.payload.dto.productdetail;

import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.ProductCategoryGeneralDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductCategoryDetailDTO {
	private Long id;
	private Long idParent;
	private String name;
	private Integer level;
	private List<ProductCategoryGeneralDTO> parents;
}
