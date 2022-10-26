package com.lucistore.lucistorebe.controller.payload.request.buyerorder;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;

@Getter
public class CreateBuyerOrderFromProductRequest extends CreateBuyerOrderRequest {
    @NotNull
    private Long idProductVariation;
    @Range(min = 1, message = "Quantity must be greater than 0")
    private Long quantity;
}
