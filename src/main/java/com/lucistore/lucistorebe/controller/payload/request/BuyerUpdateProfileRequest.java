package com.lucistore.lucistorebe.controller.payload.request;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucistore.lucistorebe.utility.EGender;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BuyerUpdateProfileRequest {
	@Size(min = 5, max = 30, message = "Full name should have at least 5 characters")
	private String fullname;
	
	@Size(min = 5, max = 30, message = "Username should have at least 5 characters")
	private String username;
	
	@Schema(type = "string", format = "email")
	@Email
	private String email;
	
	@Pattern(regexp = "0[0-9]+", message = "Phone number must contain only number character and begin with 0")
	@Size(min = 10, max = 10, message = "Phone number must have 10 number character")
	private String phone;
	
	private EGender gender;
	
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	private String otp;
}
