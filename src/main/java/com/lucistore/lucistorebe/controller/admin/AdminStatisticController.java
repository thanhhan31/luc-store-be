package com.lucistore.lucistorebe.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.StatisticService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/admi/statistic")
public class AdminStatisticController {
	@Autowired
	StatisticService statisticService;
	
	@GetMapping
	public ResponseEntity<?> statistic(
			@Parameter(description = "Search by buyer id")
			@RequestParam(required = false) List<Long> idBuyer,

			@Parameter(description = "Search by seller id")
			@RequestParam(required = false) List<Long> idAdmin,

			@Parameter(description = "Month to statistic")
			@RequestParam(required = false) Integer month,

			@Parameter(description = "Year to statistic")
			@RequestParam(required = false) Integer year
			
			// @AuthenticationPrincipal UserDetailsImpl<User> user
			){

		var r = statisticService.statistic(idBuyer, idAdmin, month, year, null);
		return ResponseEntity.ok(r);
	}
}
