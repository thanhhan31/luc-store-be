package com.lucistore.lucistorebe.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.AdminUpdateUserStatusRequest;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.service.UserService;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.ModelSorting;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/admin/user-manage")
public class AdminUserManageController {
	@Autowired
	UserService userService;
	
	@GetMapping
	public ResponseEntity<?> getAdmins(
			@RequestParam(required = false) String searchFullname,
			@RequestParam(required = false) String searchUsername,
			@RequestParam(required = false) String searchEmail,
			@RequestParam(required = false) String searchPhone,
			@RequestParam(required = false) @Schema(type = "string", allowableValues = { "ADMIN", "SALE_ADMIN" }) EUserRole role,
			@RequestParam(required = false) EUserStatus status,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Integer sortBy,
			@RequestParam(required = false) Boolean sortDescending) {
		
		return ResponseEntity.ok(
				userService.searchAdmin(
					searchFullname, 
					searchUsername, 
					searchEmail, 
					searchPhone, 
					role, status, 
					page, 
					size, 
					ModelSorting.getProductSort(sortBy, sortDescending)
				)
			);
	}
	
	@Operation(summary = "Update user account status")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PutMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<?> updateUserAccountStatus(
			@AuthenticationPrincipal UserDetailsImpl<User> user,
			@PathVariable Long id,
			@RequestBody @Valid AdminUpdateUserStatusRequest request) {
		
		return ResponseEntity.ok(userService.updateStatus(user.getUser().getId(), id, request));
	}
}
