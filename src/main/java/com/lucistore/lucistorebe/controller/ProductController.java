package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lucistore.lucistorebe.service.ProductService;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.ModelSorting;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	@Autowired
	ProductService productService;
	
	@GetMapping
	public ResponseEntity<?> get(
			@RequestParam(required = false) Long idCategory,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String searchDescription,
			@RequestParam(required = false) Long minPrice,
			@RequestParam(required = false) Long maxPrice,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Integer sortBy,
			@RequestParam(required = false) Boolean sortDescending) {
		
		return ResponseEntity.ok(
				productService.search(
					idCategory, 
					searchName, 
					searchDescription, 
					EProductStatus.ENABLED, 
					minPrice, 
					maxPrice, 
					page, 
					size, 
					ModelSorting.getProductSort(sortBy, sortDescending)
				)
			);
	}
}
