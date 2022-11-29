package com.lucistore.lucistorebe.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.BuyerEmailOtpRequest;
import com.lucistore.lucistorebe.controller.payload.request.BuyerPhoneOtpRequest;
import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.service.OtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
	@Autowired
	OtpService otpService;
	
	@Operation(summary = "Send OTP to the provided email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/email")
	public ResponseEntity<?> sendViaEmail(@RequestBody @Valid BuyerEmailOtpRequest body) {
		otpService.sendOtpViaEmail(body.getEmail());
		return ResponseEntity.ok(new BaseResponse());
	}
	
	@Operation(summary = "Send OTP to the provided phone number")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/phone")
	public ResponseEntity<?> sendViaPhone(@RequestBody @Valid BuyerPhoneOtpRequest body) {
		otpService.sendOtpViaPhone(body.getPhoneNumber());
		return ResponseEntity.ok(new BaseResponse());
	}
}
