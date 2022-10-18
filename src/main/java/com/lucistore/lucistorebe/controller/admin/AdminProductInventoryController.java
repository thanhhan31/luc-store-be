package com.lucistore.lucistorebe.controller.admin;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.payload.request.CreateProductInventoryRequest;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.service.ProductInventoryService;
import com.lucistore.lucistorebe.utility.ModelSorting;

import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api/sell-admin/inventory")
public class AdminProductInventoryController {

    @Autowired
    ProductInventoryService productInventoryService;

    @GetMapping
	public ResponseEntity<?> get(
			
			@Parameter(description = "Search by product id")
			@RequestParam(required = false) Long idProduct,

			@Parameter(description = "Search by product variation id")
			@RequestParam(required = false) Long idProductVariation,

			@Parameter(description = "Search by importer id")
			@RequestParam(required = false) Long idImporter,

			@Parameter(description = "Search by import time from")
			@RequestParam(required = false) Date importDateFrom,

			@Parameter(description = "Search by import time to")
			@RequestParam(required = false) Date importDateTo,

			@RequestParam(required = false) @Min(value = 1, message = "Page number must be greater than zero") @Parameter(description = "Specify page number") Integer page,

			@RequestParam(required = false) @Parameter(description = "Specify page size") Integer size,

			@RequestParam(required = false) @Min(1) @Max(31) @Parameter(description = "Specify sort by condition"
					+ "<br>sortBy value:\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 1: &nbsp;&nbsp;by id\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 2: &nbsp;&nbsp;by variation\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 4: &nbsp;&nbsp;by importer\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 8: &nbsp;&nbsp;by import time\r\n"
					+ "<br>\r\n"
					+ "&nbsp;&nbsp;&nbsp;&nbsp; 16: &nbsp;&nbsp;by import quantity\r\n"
					+ "<br>\r\n"
					+ "<br><i>Example: sortBy = (1+2+4) = 7 => sort by id, variation and importer attribute</i>") Integer sortBy,

			@RequestParam(required = false) @Parameter(description = "Specify sort order. True for sort in descending order") Boolean sortDescending) {

		return ResponseEntity.ok(
				productInventoryService.search(
						idProduct, idProductVariation, idImporter, importDateFrom, importDateTo,
						page, size,
						ModelSorting.getProductInventorySort(sortBy, sortDescending)));
	}

	@PostMapping("/{idProductVariation}")
	public ResponseEntity<?> create(@PathParam("idProductVariation") Long idProductVariation, @AuthenticationPrincipal UserDetailsImpl<User> user, @RequestBody @Valid CreateProductInventoryRequest body) {
		return ResponseEntity.ok(productInventoryService.create(idProductVariation, user.getUser().getId(), body));
	}
}