package com.lucistore.lucistorebe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.UpdateUserRolePermissionRequest;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.service.UserRoleService;

@RestController
@RequestMapping("/api/admin/sale-admin-permission")
public class AdminUserRolePermissionController {
	@Autowired
	UserRoleService userRoleService;
	
	@GetMapping
	public ResponseEntity<?> get() {
		return ResponseEntity.ok(userRoleService.getSaleAdminPermission());
	}
	
	@PutMapping
	public ResponseEntity<?> update(
			@AuthenticationPrincipal UserDetailsImpl<User> user, 
			@RequestBody UpdateUserRolePermissionRequest body) {
		return ResponseEntity.ok(userRoleService.updateSaleAdminPermission(user.getUser().getId(), body));
	}
}
