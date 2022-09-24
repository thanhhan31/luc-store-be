package com.lucistore.lucistorebe.controller.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.productcategory.CreateProductCategoryRequest;

@RestController
@RequestMapping("/api/admin/product-category")
public class AdminProductCategoryController {
	@PostMapping
	private void create(@RequestBody CreateProductCategoryRequest body) {
		
	}
}
