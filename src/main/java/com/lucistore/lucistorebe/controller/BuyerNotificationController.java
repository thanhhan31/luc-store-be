package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.service.NotificationService;

@RestController
@RequestMapping("/api/buyer/notification")
public class BuyerNotificationController {
	@Autowired
	NotificationService notificationService;
	
	@GetMapping
	public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
		return ResponseEntity.ok(notificationService.getAllByIdUser(buyer.getUser().getId()));
	}

	// @GetMapping("/{idProduct}")
	// public ResponseEntity<?> getById(@PathVariable Long idProduct, @AuthenticationPrincipal UserDetailsImpl<Buyer> buyer) {
	// 	return ResponseEntity.ok(notificationService.get(idProduct, buyer.getUser().getId()));
	// }
}
