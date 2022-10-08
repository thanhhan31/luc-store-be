package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PasswordResetRequest {
	@Schema(type = "string", format = "email")
	@Email
	private String email;
	
	@Pattern(regexp = "0[0-9]+", message = "Phone number must contain only number character and begin with 0")
	@Size(min = 10, max = 10, message = "Phone number must have 10 number character")
	private String phone;
	
	@NotEmpty
	@Size(min = 8, max = 30, message = "Password should have at least 8 characters")
	private String newPassword;
	
	@NotEmpty
	private String otp;
}
