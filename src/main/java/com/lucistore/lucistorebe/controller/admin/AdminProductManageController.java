package com.lucistore.lucistorebe.controller.admin;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.payload.request.product.CreateProductRequest;
import com.lucistore.lucistorebe.controller.payload.request.product.UpdateProductRequest;
import com.lucistore.lucistorebe.service.ProductService;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.ModelSorting;

@RestController
@RequestMapping("/api/admin/product-manage")
public class AdminProductManageController {
	@Autowired
	ProductService productService;
	
	@GetMapping
	public ResponseEntity<?> get(
			@RequestParam(required = false) Long idCategory,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String searchDescription,
			@RequestParam(required = false) EProductStatus status,
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
					status, 
					minPrice, 
					maxPrice, 
					page, 
					size, 
					ModelSorting.getProductSort(sortBy, sortDescending)
				)
			);
	}
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> create(
			@RequestPart("info") @Valid CreateProductRequest request,
			@RequestPart("avatar") @Valid @NotEmpty MultipartFile avatar,
			@RequestPart("images") @Valid @NotEmpty List<MultipartFile> images) {
		return ResponseEntity.ok(productService.create(request, avatar, images));
	}
	
	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> update(
			@PathVariable Long id,
			@RequestPart("info") @Valid UpdateProductRequest request,
			@RequestPart("avatar") @Valid @NotEmpty MultipartFile avatar) {
		return ResponseEntity.ok(productService.update(id, request, avatar));
	}
}
