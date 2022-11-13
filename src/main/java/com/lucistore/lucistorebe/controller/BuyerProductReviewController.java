package com.lucistore.lucistorebe.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.BuyerCreateProductReviewRequest;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.ProductReviewService;

@RestController
@RequestMapping("/api/buyer/product-review")
@PreAuthorize("hasAuthority('ACTIVE_BUYER')")
public class BuyerProductReviewController {
	@Autowired
	ProductReviewService productReviewService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> create(
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer,
			@RequestPart("data") @Valid BuyerCreateProductReviewRequest request,
			@RequestPart(name = "images", required = false) @Valid @NotEmpty List<MultipartFile> images) {
		
		return ResponseEntity.ok(productReviewService.create(buyer.getUser().getId(), request, images));
	}
}
