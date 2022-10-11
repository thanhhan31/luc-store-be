package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.controller.payload.dto.productdetail.ProductCartDetailDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerCartDetailDTO {
    private Long idBuyer;
    private ProductVariationDTO productVariation;
    private Long quantity;
    
    private ProductCartDetailDTO productDetail;
}
