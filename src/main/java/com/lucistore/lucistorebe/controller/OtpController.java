package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.BuyerEmailOtpRequest;
import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.service.MailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
	@Autowired
	MailService mailService;
	
	@Operation(summary = "Send OTP to the provided email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/email")
	public ResponseEntity<?> sendViaEmail(@RequestBody BuyerEmailOtpRequest body) {
		mailService.sendMailConfirmCode(body.getEmail());
		return ResponseEntity.ok(new BaseResponse());
	}
	
	@Operation(summary = "Send OTP to the provided phone number")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/phone")
	public ResponseEntity<?> sendViaPhone(@RequestBody BuyerEmailOtpRequest body) {
		//mailService.sendMailConfirmCode(body.getEmail());
		return ResponseEntity.ok(new BaseResponse(false, "Not implement yet"));
	}
}
