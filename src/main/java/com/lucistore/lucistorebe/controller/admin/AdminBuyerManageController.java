package com.lucistore.lucistorebe.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.BuyerService;
import com.lucistore.lucistorebe.service.UserService;
import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.ModelSorting;

@RestController
@RequestMapping("/api/admin/user-manage/buyer")
public class AdminBuyerManageController {
	@Autowired
	UserService userService;
	
	@Autowired
	BuyerService buyerService;
	
	@GetMapping
	public ResponseEntity<?> getBuyers(
			@RequestParam(required = false) String searchFullname,
			@RequestParam(required = false) String searchUsername,
			@RequestParam(required = false) String searchEmail,
			@RequestParam(required = false) String searchPhone,
			@RequestParam(required = false) EUserStatus status,
			@RequestParam(required = false) Date dob,
			@RequestParam(required = false) EGender gender,
			@RequestParam(required = false) Boolean emailConfirmed,
			@RequestParam(required = false) Boolean phoneConfirmed,
			@RequestParam(required = false) Date createdDate,
			@RequestParam(required = false) String lastModifiedBy,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Integer sortBy,
			@RequestParam(required = false) Boolean sortDescending) {

		return ResponseEntity.ok(
				buyerService.searchBuyer(
					searchFullname, 
					searchUsername, 
					searchEmail, 
					searchPhone, 
					status, 
					dob, 
					gender, 
					emailConfirmed, 
					phoneConfirmed, 
					createdDate, 
					lastModifiedBy, 
					page, 
					size, 
					ModelSorting.getProductSort(sortBy, sortDescending)
				)
			);
	}
}
