package com.lucistore.lucistorebe.controller.payload.request.buyerorder;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter
public class CreateBuyerOrderFromCartRequest extends CreateBuyerOrderRequest {
    @NotEmpty 
    private Set<Long> idProductVariations;
}
