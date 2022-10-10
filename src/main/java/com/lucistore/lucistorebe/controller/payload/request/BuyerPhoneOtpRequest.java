package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BuyerPhoneOtpRequest {
	@NotEmpty
	@Pattern(regexp = "0[0-9]+", message = "Phone number must contain only number character and begin with 0")
	@Size(min = 10, max = 10, message = "Phone number must have 10 number character")
	private String phoneNumber;
}
