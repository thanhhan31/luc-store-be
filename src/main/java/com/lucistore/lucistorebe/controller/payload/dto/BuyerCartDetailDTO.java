package com.lucistore.lucistorebe.controller.payload.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerCartDetailDTO {
    private Long idBuyer;
    private ProductVariationDTO productVariant;
    private Long quantity;
}
