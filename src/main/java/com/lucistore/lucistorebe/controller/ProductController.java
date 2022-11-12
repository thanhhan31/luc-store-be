package com.lucistore.lucistorebe.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/product")
public class ProductController {
	@Autowired
	ProductService productService;
	
	@Operation(summary = "Get product list with given search criteria. This API is for buyer, so any disabled product are invisible to this API.")
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
			@Min(1) @Max(31)
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
					+ "<br><i>Example: sortBy = (1+2+4) = 7 => sort by price, nvisit and nsold attribute</i>")
				Integer sortBy,
			@RequestParam(required = false) 
			@Parameter(description = "Specify sort order. True for sort in descending order")
				Boolean sortDescending) {
		
		return ResponseEntity.ok(
			productService.search(
				new ProductFilter(
					productService.getAllParentCategories(idCategory, true), 
					searchName, 
					searchDescription, 
					EProductStatus.ENABLED, 
					minPrice, 
					maxPrice
				),
				new PagingInfo(page, size, sortBy, sortDescending)
			)
		);
	}
	
	@Operation(summary = "Get top 10 lasted product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/lasted")
	public ResponseEntity<?> getTopLasted() {
		return ResponseEntity.ok(productService.getTopLastedProduct());
	}
	
	@Operation(summary = "Get top 10 most viewed product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/most-viewed")
	public ResponseEntity<?> getTopViewed() {
		return ResponseEntity.ok(productService.getTopVisitProduct());
	}
	
	@Operation(summary = "Get top 10 most sold product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/most-sold")
	public ResponseEntity<?> getTopSold() {
		return ResponseEntity.ok(productService.getTopSoldProduct());
	}
	
	@Operation(summary = "Get product detail with given id. This API is for buyer, so any disabled product are invisible to this API.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successful",
					content = { @Content(mediaType = "application/json") })
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> getDetails(@PathVariable("id") long id) {
		return ResponseEntity.ok(productService.getById(id, true));
	}
}
