package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.lucistore.lucistorebe.utility.EGender;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SignupUsernamePasswordRequest {
	@NotEmpty
	@Size(min = 5, max = 30, message = "Full name should have at least 5 characters")
	private String fullname;
	
	@NotEmpty
	@Size(min = 5, max = 30, message = "Username should have at least 5 characters")
	private String username;
	
	@NotEmpty
	@Size(min = 8, max = 30, message = "Password should have at least 8 characters")
	private String password;
	
	@NotEmpty
	@Schema(type = "string", format = "email")
	@Email
	private String email;
	
	private EGender gender;
}
