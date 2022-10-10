package com.lucistore.lucistorebe.controller.payload.request.buyercartdetail;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CreateBuyerCartDetailRequest {
    @NotNull
    private Long idProductVariation;
    @NotNull @Min(1)
    private Long quantity;
}
