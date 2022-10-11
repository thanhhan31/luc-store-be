package com.lucistore.lucistorebe.controller.payload.request.buyercartdetail;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UpdateBuyerCartDetailRequest {
    @Range(min = 1, message = "Quantity must be greater than 0")
    private Long quantity;
}
