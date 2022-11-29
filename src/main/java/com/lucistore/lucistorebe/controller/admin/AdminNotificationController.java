package com.lucistore.lucistorebe.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.CreateNotificationRequest;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.service.NotificationService;

@RestController
@RequestMapping("/api/admin/notification")
public class AdminNotificationController {
	@Autowired
	NotificationService notificationService;
	
	@GetMapping
	public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetailsImpl<User> user) {
		return ResponseEntity.ok(notificationService.getAllByIdUser(user.getUser().getId()));
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid CreateNotificationRequest data) {
		return ResponseEntity.ok(notificationService.create(data.getIdUser(), data.getTitle(), data.getContent()));
	}

	// @GetMapping("/{idProduct}")
	// public ResponseEntity<?> getById(@PathVariable Long idProduct, @AuthenticationPrincipal UserDetailsImpl<User> user) {
	// 	return ResponseEntity.ok(notificationService.get(idProduct, user.getUser().getId()));
	// }
}
