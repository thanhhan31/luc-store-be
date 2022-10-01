package com.lucistore.lucistorebe.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.BuyerUpdateProfileRequest;
import com.lucistore.lucistorebe.controller.payload.request.PasswordUpdateRequest;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.BuyerService;

@RestController
@RequestMapping("/api/buyer/profile")
public class BuyerProfileController {
	@Autowired
	BuyerService buyerService;
	
	@GetMapping
	public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		Buyer cast = buyer.getUser();
		return ResponseEntity.ok(buyerService.getById(cast.getId()));
	}
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> update(
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer,
			@RequestPart(value = "info", required = false) @Valid BuyerUpdateProfileRequest request,
			@RequestPart(name = "avatar", required = false) MultipartFile avatar) {
		
		return ResponseEntity.ok(buyerService.update(buyer.getUser().getId(), request, avatar));
	}
	
	@PutMapping("/password")
	public ResponseEntity<?> updatePassword(
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer,
			@RequestBody @Valid PasswordUpdateRequest body) {
		
		return ResponseEntity.ok(buyerService.updatePassword(buyer.getUser().getId(), body));
	}
}
