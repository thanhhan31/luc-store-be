package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.SignupUsernamePasswordRequest;
import com.lucistore.lucistorebe.service.BuyerService;

@RestController
@RequestMapping("/api/buyer/signup")
public class SignupController {
	@Autowired
	BuyerService buyerService;
	
	@PostMapping
	public ResponseEntity<?> signup(@RequestBody SignupUsernamePasswordRequest body) {
		return ResponseEntity.ok(buyerService.create(body));
	}
}
