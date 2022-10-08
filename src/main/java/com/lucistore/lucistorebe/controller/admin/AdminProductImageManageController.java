package com.lucistore.lucistorebe.controller.admin;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.service.ProductImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/admin/product-manage/{idProduct}/image")
public class AdminProductImageManageController {
	@Autowired
	ProductImageService productImageService;
	
	@Operation(summary = "Add (create) new product images to given product id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> create(
			@PathVariable Long idProduct,
			@RequestPart("images") @Valid @NotEmpty List<MultipartFile> images) {
		return ResponseEntity.ok(productImageService.create(idProduct, images));
	}
	
	@Operation(summary = "Delete multiple product image with given array of id product image")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> delete(
			@PathVariable Long idProduct,
			@RequestBody List<Long> ids) {
		productImageService.delete(idProduct, ids);
		return ResponseEntity.ok(new BaseResponse());
	}
}
