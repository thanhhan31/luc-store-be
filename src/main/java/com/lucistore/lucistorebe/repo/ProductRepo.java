package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
	
}
