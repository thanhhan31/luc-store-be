package com.lucistore.lucistorebe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.BuyerService;
import com.lucistore.lucistorebe.service.UserService;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.ModelSorting;

import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/admin/user-manage")
public class AdminUserManageController {
	@Autowired
	UserService userService;
	
	@Autowired
	BuyerService buyerService;
	
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
}