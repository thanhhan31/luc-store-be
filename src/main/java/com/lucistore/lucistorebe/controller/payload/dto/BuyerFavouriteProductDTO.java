package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.controller.payload.dto.productdetail.ProductDetailDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerFavouriteProductDTO {
	private BuyerDTO buyer;
	private ProductDetailDTO product;
}
