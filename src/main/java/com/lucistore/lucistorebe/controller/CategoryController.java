package com.lucistore.lucistorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.ProductCategoryService;

@RestController
@RequestMapping("/api/product-category")
public class CategoryController {
	@Autowired
	ProductCategoryService productCategoryService;
	
	//@PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'CREATE_ORDER')")
	@GetMapping
	public ResponseEntity<?> getAllRootCategories() {
		return ResponseEntity.ok(productCategoryService.getAllRootCategories(true));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(productCategoryService.get(id, true));
	}
}
