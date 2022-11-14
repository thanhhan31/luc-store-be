package com.lucistore.lucistorebe.controller.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CreatePaymentResponse extends BaseResponse {
	private String payUrl;

	public CreatePaymentResponse(String payUrl) {
		this.payUrl = payUrl;
	}
}