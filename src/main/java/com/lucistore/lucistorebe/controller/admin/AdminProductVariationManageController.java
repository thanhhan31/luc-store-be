package com.lucistore.lucistorebe.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.product.CreateProductVariationRequest;
import com.lucistore.lucistorebe.controller.payload.request.product.UpdateProductVariationRequest;
import com.lucistore.lucistorebe.service.ProductVariationService;

@RestController
@RequestMapping("/api/admin/product-manage/{idProduct}/variation")
public class AdminProductVariationManageController {
	@Autowired
	ProductVariationService productVariationService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(
			@PathVariable Long idProduct,
			@RequestBody @Valid CreateProductVariationRequest body) {
		
		return ResponseEntity.ok(productVariationService.create(idProduct, body));
	}
	
	@PutMapping(name = "/{idVariation}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(
			@PathVariable Long idProduct,
			@PathVariable Long idVariation,
			@RequestBody @Valid UpdateProductVariationRequest body) {
		
		return ResponseEntity.ok(productVariationService.update(idVariation, idProduct, body));
	}
}
