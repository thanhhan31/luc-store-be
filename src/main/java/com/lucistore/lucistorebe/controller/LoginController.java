package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.LoginKeyPasswordRequest;
import com.lucistore.lucistorebe.controller.payload.response.LoginResponse;
import com.lucistore.lucistorebe.service.auth.LoginService;
import com.lucistore.lucistorebe.utility.EUserRole;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class LoginController {
	@Autowired
	LoginService loginService;
	
	@Operation(
			summary = "Login API for admin",
			description = "Login key can be username, phone number or email that user is registered.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/admin/login")
	public ResponseEntity<?> admin(@RequestBody LoginKeyPasswordRequest body) {
		return ResponseEntity.ok(login(body, EUserRole.ADMIN));
	}
	
	@Operation(
			summary = "Login API for buyer",
			description = "Login key can be username, phone number or email that user is registered.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping("/buyer/login")
	public ResponseEntity<?> buyer(@RequestBody LoginKeyPasswordRequest body) {
		return ResponseEntity.ok(login(body, EUserRole.BUYER));
	}
	
	public LoginResponse<?> login(LoginKeyPasswordRequest body, EUserRole r) {
		return loginService.authenticateWithUsernamePassword(body.getLoginKey(), body.getPassword(), r);
	}
}
