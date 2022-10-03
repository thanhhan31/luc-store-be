package com.lucistore.lucistorebe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.controller.payload.request.buyerrank.UpdateBuyerRankRequest;
import com.lucistore.lucistorebe.service.BuyerRankService;

@RestController
@RequestMapping("/api/admin/buyer-rank")
public class AdminBuyerRankController {
	@Autowired
	BuyerRankService buyerRankService;
	
	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(buyerRankService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return ResponseEntity.ok(buyerRankService.getById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateBuyerRankRequest body) {
		return ResponseEntity.ok(buyerRankService.update(id, body));
	}
}
