package com.lucistore.lucistorebe.utility.filter;

import java.util.Date;

import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.EPaymentMethod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderFilter {
	Long idBuyer;
	Long idSeller;
	Long idDeliveryAddress;
	Date createTime;
	EOrderStatus status;
	EPaymentMethod paymentMethod;
	Boolean isAllOrderDetailReviewed;
	
	public OrderFilter(Long idBuyer, Long idSeller, Long idDeliveryAddress, Date createTime, EOrderStatus status,
			EPaymentMethod paymentMethod, Boolean isAllOrderDetailReviewed) {
		this.idBuyer = idBuyer;
		this.idSeller = idSeller;
		this.idDeliveryAddress = idDeliveryAddress;
		this.createTime = createTime;
		this.status = status;
		this.paymentMethod = paymentMethod;
		this.isAllOrderDetailReviewed = isAllOrderDetailReviewed;
	}
}
