package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BuyerOtpConfirmRequest {
	@NotEmpty
	private String otp;
}
