package com.lucistore.lucistorebe.controller.payload.dto.order;

import java.util.Date;
import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.BuyerDeliveryAddressDTO;
import com.lucistore.lucistorebe.controller.payload.dto.UserDTO;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.EPaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerDetailedOrderDTO {
	
	private Long id;

	private Date createTime;

	private Long payPrice;

	private Long price;
    
	private String note;
	
	private EOrderStatus status;
	
	private EPaymentMethod paymentMethod;
	
	private List<OrderDetailDTO> orderDetails;
	
	private UserDTO seller;
	
	private BuyerDeliveryAddressDTO deliveryAddress;
	
}
