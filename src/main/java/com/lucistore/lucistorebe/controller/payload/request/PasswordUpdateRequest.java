package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordUpdateRequest {
	@NotEmpty
	@Size(min = 8, max = 30, message = "Password should have at least 8 characters")
	private String newPassword;
	
	private String oldPassword;
	
	@NotEmpty
	private String otp;
}
