package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.Date;

import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.EPaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDTO {
	private Long id;
	
	private BuyerDTO buyer;
	
	private UserDTO seller;
	
	private BuyerDeliveryAddressDTO deliveryAddress;

	private Date createTime;
    
	private String note;
	
	private EOrderStatus status;
	
	private EPaymentMethod paymentMethod;
	
	private Boolean reviewed;
}
