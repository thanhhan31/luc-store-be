package com.lucistore.lucistorebe.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.service.StatisticService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/admin/statistic")
public class AdminStatisticController {
	@Autowired
	StatisticService statisticService;
	
	
	@GetMapping
	public ResponseEntity<?> statistic(
			@Parameter(description = "Search by product id")
			@RequestParam(required = false) List<Long> idProduct,

			@Parameter(description = "Search by product variation id")
			@RequestParam(required = false) List<Long> idProductVariation,

			@Parameter(description = "Search by buyer id")
			@RequestParam(required = false) List<Long> idBuyer,

			@Parameter(description = "Search by seller id")
			@RequestParam(required = false) List<Long> idAdmin,

			@Parameter(description = "Search by import time from")
			@RequestParam(required = false) Date importDateFrom,

			@Parameter(description = "Search by import time to")
			@RequestParam(required = false) Date importDateTo,
			
			@AuthenticationPrincipal UserDetailsImpl<User> user
			){

		var r = statisticService.statistic(idProduct, idProductVariation, idBuyer, idAdmin, importDateFrom, importDateTo, user.getUser());
		return ResponseEntity.ok(r);
	}
}
