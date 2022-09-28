package com.lucistore.lucistorebe.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.BuyerService;

@RestController
@RequestMapping("/api/buyer/profile")
public class BuyerProfileController {
	@Autowired
	BuyerService buyerService;
	
	@GetMapping
	public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetailsImpl buyer) {
		Buyer cast = (Buyer)buyer.getUserInfo();
		return ResponseEntity.ok(buyerService.getById(cast.getId()));
	}
}
