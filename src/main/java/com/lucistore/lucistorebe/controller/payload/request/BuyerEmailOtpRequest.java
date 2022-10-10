package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BuyerEmailOtpRequest {
	@NotEmpty
	@Schema(type = "string", format = "email")
	@Email
	private String email;
}
