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

@RestController
@RequestMapping("/api")
public class LoginController {
	@Autowired
	LoginService loginService;
	
	@PostMapping("/admin/login")
	private ResponseEntity<?> admin(@RequestBody LoginKeyPasswordRequest body) {
		return ResponseEntity.ok(login(body, EUserRole.ADMIN));
	}
	
	@PostMapping("/buyer/login")
	private ResponseEntity<?> buyer(@RequestBody LoginKeyPasswordRequest body) {
		return ResponseEntity.ok(login(body, EUserRole.BUYER));
	}
	
	private LoginResponse<?> login(LoginKeyPasswordRequest body, EUserRole r) {
		return loginService.authenticateWithUsernamePassword(body.getLoginKey(), body.getPassword(), r);
	}
}
