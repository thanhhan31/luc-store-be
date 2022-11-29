package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.ProductReviewService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/product/{idProduct}/review")
public class ProductReviewController {
	@Autowired
	ProductReviewService productReviewService;
	
	@GetMapping
	public ResponseEntity<?> get(
			@PathVariable("idProduct") 
				Long idProduct,
			@RequestParam(required = false) 
			@Parameter(description = "Specify page number")
				Integer page,
			@RequestParam(required = false) 
			@Parameter(description = "Specify page size")
				Integer size,
			@RequestParam(required = false) 
			@Parameter(description = "True if sort by oldest, else sort by lasted")
				Boolean sortByOldest) {
		
		return ResponseEntity.ok(productReviewService.getReviews(idProduct, page, size, sortByOldest));
	}
}
