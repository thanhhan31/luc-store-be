package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.NotNull;

import com.lucistore.lucistorebe.utility.EPaymentMethod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BuyerUpdateOrderPaymentMethodRequest {
	
	@NotNull
	EPaymentMethod newPaymentMethod;
	
}
