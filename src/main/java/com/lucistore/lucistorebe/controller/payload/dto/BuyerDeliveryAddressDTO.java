package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.controller.payload.dto.address.AddressWardDTO;
import com.lucistore.lucistorebe.utility.EBuyerDeliveryAddressStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerDeliveryAddressDTO {
	private Long id;
	private BuyerDTO buyer;
	private AddressWardDTO addressWard;
	private String addressDetail;
	private String receiverName;
	private String receiverPhone;
	private Boolean isDefault;
	private EBuyerDeliveryAddressStatus status;
}