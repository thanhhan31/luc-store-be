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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.service.ProductImageService;

@RestController
@RequestMapping("/api/admin/product-manage/{idProduct}/image")
public class AdminProductImageManageController {
	@Autowired
	ProductImageService productImageService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> create(
			@PathVariable Long idProduct,
			@RequestPart("images") @Valid @NotEmpty List<MultipartFile> images) {
		return ResponseEntity.ok(productImageService.create(idProduct, images));
	}
	
	@DeleteMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> delete(
			@PathVariable Long idProduct,
			@RequestParam List<Long> ids) {
		productImageService.delete(idProduct, ids);
		return ResponseEntity.ok(new BaseResponse());
	}
}
