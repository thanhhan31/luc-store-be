package com.lucistore.lucistorebe.controller.payload.request;

import lombok.Getter;

@Getter
public class CreateProductInventoryRequest {
    private Long importQuantity;
    private Long idProductVariation;
}
