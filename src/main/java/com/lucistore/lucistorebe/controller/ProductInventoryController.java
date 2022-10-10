package com.lucistore.lucistorebe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/sell-admin/inventory")
public class ProductInventoryController {
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
            
        );
    }
    
}
