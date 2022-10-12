package com.lucistore.lucistorebe.controller.payload.request.buyerdeliveryaddress;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UpdateBuyerDeliveryAddressRequest {
    @NotNull(message = "idAddressWard is required")
    private Long idAddressWard;

    @NotNull(message = "addressDetail is required")
    private String addressDetail;

    @NotNull(message = "receiverName is required")
    private String receiverName;

    @NotNull(message = "receiverPhone is required")
    private String receiverPhone;

    @NotNull(message = "isDefault is required")
    private Boolean isDefault;
}
