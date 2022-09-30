package com.lucistore.lucistorebe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.productcategory.CreateProductCategoryRequest;
import com.lucistore.lucistorebe.controller.payload.request.productcategory.UpdateProductCategoryRequest;
import com.lucistore.lucistorebe.service.ProductCategoryService;

@RestController
@RequestMapping("/api/admin/product-category")
public class AdminUserManageController {
	@Autowired
	ProductCategoryService productCategoryService;
	
	@GetMapping
	public ResponseEntity<?> getAllRootCategories() {
		return ResponseEntity.ok(productCategoryService.getAllRootCategories());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(productCategoryService.get(id));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody CreateProductCategoryRequest body) {
		return ResponseEntity.ok(productCategoryService.create(body));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateProductCategoryRequest body) {
		return ResponseEntity.ok(productCategoryService.update(id, body));
	}
}
