package com.lucistore.lucistorebe.controller.admin;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.product.CreateProductRequest;
import com.lucistore.lucistorebe.controller.payload.request.product.UpdateProductRequest;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.service.ProductService;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.ProductFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/admin/product-manage")
public class AdminProductManageController {
	@Autowired
	ProductService productService;
	
	@Operation(summary = "Get product list with given search criteria.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping
	public ResponseEntity<?> get(
			@RequestParam(required = false) 
			@Parameter(description = "Specify category id to search. Result will include subcategories if has.")
				Long idCategory,
			@RequestParam(required = false) 
			@Parameter(description = "Search product name")
				String searchName,
			@RequestParam(required = false) 
			@Parameter(description = "Search product description")
				String searchDescription,
			@RequestParam(required = false)
			@Parameter(description = "Specify status of product")
				EProductStatus status,
			@RequestParam(required = false) 
			@Parameter(description = "Limit minimum price")
				Long minPrice,
			@RequestParam(required = false) 
			@Parameter(description = "Limit maximum price")
				Long maxPrice,
			@RequestParam(required = false) 
			@Min(value = 1, message = "Page number must be greater than zero")
			@Parameter(description = "Specify page number")
				Integer page,
			@RequestParam(required = false) 
			@Parameter(description = "Specify page size")
				Integer size,
			@RequestParam(required = false) 
			@Min(1) @Max(63)
			@Parameter(description = "Specify sort by condition"
					+ "<br>sortBy value:\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 1: &nbsp;&nbsp;by price\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 2: &nbsp;&nbsp;by nvisit\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 4: &nbsp;&nbsp;by nsold\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 8: &nbsp;&nbsp;by createdDate\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 16: by lastModifiedDate"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 32: by maxDiscount"
					+ "<br><i>Example: sortBy = (1+2+4) = 7 => sort by price, nvisit and nsold attribute</i>")
				Integer sortBy,
			@RequestParam(required = false) 
			@Parameter(description = "Specify sort order. True for sort in descending order")
				Boolean sortDescending) {
		
		return ResponseEntity.ok(
			productService.search(
				new ProductFilter(
					productService.getAllParentCategories(idCategory, false), 
					searchName, 
					searchDescription, 
					status, 
					minPrice, 
					maxPrice
				),
				new PagingInfo(page, size, sortBy, sortDescending)
			)
		);
	}
	
	@Operation(summary = "Get product detail with given id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> getDetails(@PathVariable("id") long id) {
		return ResponseEntity.ok(productService.getById(id, false));
	}
	
	@Operation(summary = "Create new product with given information. New product will have status of DISABLED as default status.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> create(
			@AuthenticationPrincipal UserDetailsImpl<User> user,
			@RequestPart("info") @Valid CreateProductRequest request,
			@RequestPart("avatar") @Valid @NotEmpty MultipartFile avatar,
			@RequestPart("images") @Valid @NotEmpty List<MultipartFile> images) {
		return ResponseEntity.ok(productService.create(user.getUser().getId(), request, avatar, images));
	}
	
	@Operation(summary = "Update product's information with given id and information")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> update(
			@AuthenticationPrincipal UserDetailsImpl<User> user,
			@PathVariable Long id,
			@RequestPart(name = "info", required = false) @Valid UpdateProductRequest request,
			@RequestPart(name = "avatar", required = false) MultipartFile avatar) {
		return ResponseEntity.ok(productService.update(user.getUser().getId(), id, request, avatar));
	}
}
