package com.lucistore.lucistorebe.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.buyerdeliveryaddress.CreateBuyerDeliveryAddressRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerdeliveryaddress.UpdateBuyerDeliveryAddressRequest;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.BuyerDeliveryAddressService;

@RestController
@RequestMapping("/api/buyer/delivery-address")
@PreAuthorize("hasAuthority('ACTIVE_BUYER')")
public class BuyerDeliveryAddressController {
	@Autowired
	BuyerDeliveryAddressService buyerDeliveryAddressService;
	
	@GetMapping
	public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerDeliveryAddressService.getAllByIdBuyer(buyer.getUser().getId()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerDeliveryAddressService.get(id, buyer.getUser().getId()));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UpdateBuyerDeliveryAddressRequest body,
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerDeliveryAddressService.update(id, buyer.getUser().getId(), body));
	}

	@PostMapping
	public ResponseEntity<?> create(@PathVariable Long id, @RequestBody @Valid CreateBuyerDeliveryAddressRequest body,
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerDeliveryAddressService.create(buyer.getUser().getId(), body));
	}
}
