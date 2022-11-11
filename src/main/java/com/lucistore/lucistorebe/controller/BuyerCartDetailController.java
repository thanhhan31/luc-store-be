package com.lucistore.lucistorebe.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.buyercartdetail.CreateBuyerCartDetailRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyercartdetail.UpdateBuyerCartDetailRequest;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.BuyerCartDetailService;

@RestController
@RequestMapping("/api/buyer/cart-detail")
@PreAuthorize("hasAuthority('ACTIVE_BUYER')")
public class BuyerCartDetailController {
	@Autowired
	BuyerCartDetailService buyerCartDetailService;
	
	@GetMapping
	public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerCartDetailService.getAllByIdBuyer(buyer.getUser().getId()));
	}

	@GetMapping("/{idProductVariation}")
	public ResponseEntity<?> getById(@PathVariable Long idProductVariation, @AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerCartDetailService.get(idProductVariation, buyer.getUser().getId()));
	}
	
	@PutMapping("/{idProductVariation}")
	public ResponseEntity<?> update(@PathVariable Long idProductVariation, @RequestBody @Valid UpdateBuyerCartDetailRequest body,
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerCartDetailService.update(idProductVariation, buyer.getUser().getId(), body));
	}

	@PostMapping("/{idProductVariation}")
	public ResponseEntity<?> create(@PathVariable Long idProductVariation, @RequestBody @Valid CreateBuyerCartDetailRequest body,
			@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerCartDetailService.create(idProductVariation, buyer.getUser().getId(), body));
	}

	@DeleteMapping("/{idProductVariation}")
	public ResponseEntity<?> delete(@PathVariable Long idProductVariation, @AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerCartDetailService.delete(idProductVariation, buyer.getUser().getId()));
	}

	@GetMapping("/count")
	public ResponseEntity<?> count(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(buyerCartDetailService.countItemByBuyer(buyer.getUser()));
	}
}
