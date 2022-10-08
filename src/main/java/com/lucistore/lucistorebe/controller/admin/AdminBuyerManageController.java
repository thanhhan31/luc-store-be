package com.lucistore.lucistorebe.controller.admin;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.AdminUpdateUserStatusRequest;
import com.lucistore.lucistorebe.service.BuyerService;
import com.lucistore.lucistorebe.service.UserService;
import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.EUserStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/admin/user-manage/buyer")
public class AdminBuyerManageController {
	@Autowired
	UserService userService;
	
	@Autowired
	BuyerService buyerService;
	
	@Operation(summary = "Get buyer list with given search criteria")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping(produces = "application/json")
	public ResponseEntity<?> getBuyers(
			@RequestParam(required = false) 
			@Parameter(description = "Full name of buyer to search") 
				String searchFullname,
			@RequestParam(required = false) 
			@Parameter(description = "User name of buyer to search") 
				String searchUsername,
			@RequestParam(required = false) 
			@Parameter(description = "Email of buyer to search") 
				String searchEmail,
			@RequestParam(required = false) 
			@Parameter(description = "Phone of buyer to search") 
				String searchPhone,
			@RequestParam(required = false) 
			@Parameter(description = "Buyer account status") 
				EUserStatus status,
			@RequestParam(required = false) 
			@Parameter(description = "Date of birth of buyer to search") 
				Date dob,
			@RequestParam(required = false) 
			@Parameter(description = "Gender of buyer to search") 
				EGender gender,
			@RequestParam(required = false) 
			@Parameter(description = "Search buyer whose email is confirmed or not") 
				Boolean emailConfirmed,
			@RequestParam(required = false) 
			@Parameter(description = "Search buyer whose phone is confirmed or not") 
				Boolean phoneConfirmed,
			@RequestParam(required = false) 
			@Parameter(description = "Search buyer by the date account is registered") 
				Date createdDate,
			@RequestParam(required = false) 
			@Parameter(description = "Search who (admin) modifify buyer account information") 
				String lastModifiedBy,
			@RequestParam(required = false) 
			@Parameter(description = "Specify page number")
				Integer page,
			@RequestParam(required = false) 
			@Parameter(description = "Specify page size")
				Integer size,
			@RequestParam(required = false) 
			@Min(1) @Max(31)
			@Parameter(description = "Specify sort by condition"
					+ "<br>sortBy value:\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 1: &nbsp;&nbsp;by fullname\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 2: &nbsp;&nbsp;by username\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 4: &nbsp;&nbsp;by email\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 8: &nbsp;&nbsp;by phone\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 16: by createdDate"
					+ "<br><i>Example: sortBy = (1+2+4) = 7 => sort by fullname, username and email attribute</i>")
				Integer sortBy,
			@RequestParam(required = false) @Parameter(description = "Specify sort order. True for sort in descending order.")
				Boolean sortDescending) {

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
					sortBy, 
					sortDescending
				)
			);
	}
	
	@Operation(summary = "Update buyer account status")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PutMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<?> updateBuyerAccountStatus(
			@PathVariable Long id,
			@RequestBody @Valid AdminUpdateUserStatusRequest request) {
		
		return ResponseEntity.ok(buyerService.updateStatus(id, request));
	}
}
