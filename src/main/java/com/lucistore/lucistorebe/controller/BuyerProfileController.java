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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/buyer/profile")
public class BuyerProfileController {
	@Autowired
	BuyerService buyerService;
	
	@Operation(summary = "Get current logged in buyer's profile information")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping
	public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		Buyer cast = buyer.getUser();
		return ResponseEntity.ok(buyerService.getById(cast.getId()));
	}
	
	@Operation(summary = "Update buyer profile information", 
			description = "When buyer register via OAuth (Google,...), buyer can change  username only one time."
					+ "<br>When change username user is required to login again."
					+ "<br><b><i>OTP is ony required when user want to change email or phone number.</i></b>")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "request", contentType = "application/json")))
	public ResponseEntity<?> update(
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer,
			@RequestPart(value = "info", required = false) @Valid BuyerUpdateProfileRequest request,
			@RequestPart(name = "avatar", required = false) MultipartFile avatar) {
		
		return ResponseEntity.ok(buyerService.update(buyer.getUser().getId(), request, avatar));
	}
	
	@Operation(summary = "Update buyer password", 
			description = "<b><i>If buyer doesn't have password yet (due to register via OAuth) then leave old password null.</i></b>")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PutMapping("/password")
	public ResponseEntity<?> updatePassword(
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer,
			@RequestBody @Valid PasswordUpdateRequest body) {
		
		return ResponseEntity.ok(buyerService.updatePassword(buyer.getUser().getId(), body));
	}
}
