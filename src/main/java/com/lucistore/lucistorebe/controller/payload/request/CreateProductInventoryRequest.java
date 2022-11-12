package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;

@Getter
public class CreateProductInventoryRequest {
    @NotNull
    Long idProductVariation;
    @Range(min = 1, message = "Quantity must be greater than zero")
    Long quantity;
}
