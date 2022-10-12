package com.lucistore.lucistorebe.controller.payload.request.buyerdeliveryaddress;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateBuyerDeliveryAddressRequest {
    @NotNull(message = "idAddressWard is required")
    private Long idAddressWard;

    @NotNull(message = "addressDetail is required")
    private String addressDetail;

    private String receiverName;

    private String receiverPhone;

    @NotNull(message = "isDefault is required")
    private Boolean isDefault;
}
