package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.OtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/buyer/otp")
public class BuyerOtpController {
	@Autowired
	OtpService otpService;
	
	@Operation(summary = "Send OTP to the email of current logged in buyer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/email")
	public ResponseEntity<?> sendViaEmail(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		otpService.sendOtpViaEmail(buyer.getUser());
		return ResponseEntity.ok(new BaseResponse());
	}
	
	@Operation(summary = "Send OTP to the phone number of current logged in buyer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/phone")
	public ResponseEntity<?> sendViaPhone(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		otpService.sendOtpViaPhone(buyer.getUser());
		return ResponseEntity.ok(new BaseResponse());
	}
}
