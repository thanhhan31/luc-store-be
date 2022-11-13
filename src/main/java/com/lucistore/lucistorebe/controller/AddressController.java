package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/address")
public class AddressController {
	@Autowired
	AddressService addressService;
	
	@Operation(summary = "Get list of province and city")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping
	public ResponseEntity<?> getProvinceCityList() {
		return ResponseEntity.ok(addressService.getProvinceCityList());
	}
	
	@Operation(summary = "Get list of district by given id of province/city")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/province-city/{id}")
	public ResponseEntity<?> getProvinceCityById(@PathVariable Long id) {
		return ResponseEntity.ok(addressService.getProvinceCityById(id));
	}
	
	@Operation(summary = "Get list of ward by given id of district")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/district/{id}")
	public ResponseEntity<?> getDistrictById(@PathVariable Long id) {
		return ResponseEntity.ok(addressService.getDistrictById(id));
	}
}
