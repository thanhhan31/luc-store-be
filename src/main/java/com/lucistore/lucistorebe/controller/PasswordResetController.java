package com.lucistore.lucistorebe.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.PasswordResetRequest;
import com.lucistore.lucistorebe.service.BuyerService;

@RestController
@RequestMapping("/api/buyer/reset-password")
public class PasswordResetController {
	@Autowired
	BuyerService buyerService;
	
	@PostMapping
	public ResponseEntity<?> shopSendVerifyToken(@RequestBody @Valid PasswordResetRequest request) {
		return ResponseEntity.ok(buyerService.resetPassword(request));
	}
}
