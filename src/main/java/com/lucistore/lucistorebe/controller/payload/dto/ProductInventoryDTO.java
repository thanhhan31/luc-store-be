package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductInventoryDTO {
    private Long id;
    private Date importTime;
    private UserDTO importer;
    private Long importQuantity;

    private ProductVariationDTO variation;
}
