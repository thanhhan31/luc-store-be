package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;

@Getter
public class CreateProductInventoryRequest {
    @Range(min = 1, message = "Import quantity must be greater than 0")
    private Long importQuantity;
    private Long idProductVariation;
}
