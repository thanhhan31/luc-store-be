package com.lucistore.lucistorebe.controller.admin;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.OrderService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderManageController {
	@Autowired
	OrderService orderService;
	
	@GetMapping
	public ResponseEntity<?> search(
		@RequestParam(required = false) 
		@Min(value = 1, message = "Page number must be greater than zero")
		@Parameter(description = "Specify page number")
			Integer page,
		@RequestParam(required = false) 
		@Parameter(description = "Specify page size")
			Integer size
	) {
		return ResponseEntity.ok(orderService.get(page, size));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.get(id, null));
	}
}
