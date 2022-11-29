package com.lucistore.lucistorebe.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.productcategory.CreateProductCategoryRequest;
import com.lucistore.lucistorebe.controller.payload.request.productcategory.UpdateProductCategoryRequest;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.service.ProductCategoryService;

@RestController
@RequestMapping("/api/admin/product-category")
public class AdminProductCategoryManageController {
	@Autowired
	ProductCategoryService productCategoryService;
	
	@GetMapping
	public ResponseEntity<?> getAllRootCategories() {
		return ResponseEntity.ok(productCategoryService.getAllRootCategories(false));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(productCategoryService.get(id, false));
	}
	
	@PostMapping
	public ResponseEntity<?> create(
			@AuthenticationPrincipal UserDetailsImpl<User> user, 
			@RequestBody @Valid CreateProductCategoryRequest body) {
		return ResponseEntity.ok(productCategoryService.create(user.getUser().getId(), body));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(
			@AuthenticationPrincipal UserDetailsImpl<User> user, 
			@PathVariable Long id, 
			@RequestBody @Valid UpdateProductCategoryRequest body) {
		return ResponseEntity.ok(productCategoryService.update(user.getUser().getId(), id, body));
	}
}
