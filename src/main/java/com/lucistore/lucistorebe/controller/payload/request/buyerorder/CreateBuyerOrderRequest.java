package com.lucistore.lucistorebe.controller.payload.request.buyerorder;

import javax.validation.constraints.NotNull;

import com.lucistore.lucistorebe.utility.EPaymentMethod;

import lombok.Getter;

@Getter
public class CreateBuyerOrderRequest {
    private String note;
    @NotNull
    private Long idAddress;
    @NotNull
    private EPaymentMethod paymentMethod;
}
