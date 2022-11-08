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
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderFromCartRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderFromProductRequest;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.OrderService;

@RestController
@RequestMapping("/api/buyer/order")
public class BuyerOrderController {
	@Autowired
	OrderService orderService;
	
	//@PreAuthorize("hasAnyAuthority('ACTIVE_BUYER', 'BANNED_BUYER')")
	@GetMapping
	public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(orderService.getAllByIdBuyer(buyer.getUser().getId()));
	}

	//@PreAuthorize("hasAnyAuthority('ACTIVE_BUYER', 'BANNED_BUYER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(orderService.get(id, buyer.getUser().getId()));
	}
	
	//@PreAuthorize("hasAnyAuthority('ACTIVE_BUYER', 'BANNED_BUYER')")
	@PutMapping("/cancel/{id}")
	public ResponseEntity<?> cancel(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(orderService.cancel(id, buyer.getUser().getId()));
	}

	@PreAuthorize("hasAuthority('ACTIVE_BUYER')")
	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid CreateBuyerOrderFromProductRequest body,
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(orderService.create(buyer.getUser().getId(), body));
	}

	@PreAuthorize("hasAuthority('ACTIVE_BUYER')")
	@PostMapping("/cart")
	public ResponseEntity<?> create(@RequestBody @Valid CreateBuyerOrderFromCartRequest body,
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(orderService.create(buyer.getUser().getId(), body));
	}
}
