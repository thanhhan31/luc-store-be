package com.lucistore.lucistorebe.controller.payload.dto.order;

import java.util.Date;
import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.BuyerDTO;
import com.lucistore.lucistorebe.controller.payload.dto.UserDTO;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.EPaymentMethod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AdminOrderDTO {
	
	private Long id;
	
	private Long idDeliveryAddress;

	private Date createTime;

	private Long payPrice;

	private Long price;
    
	private String note;
	
	private EOrderStatus status;
	
	private EPaymentMethod paymentMethod;
	
	private BuyerDTO buyer;
	
	private UserDTO seller;
	
	private List<OrderDetailDTO> orderDetails;
	
}
