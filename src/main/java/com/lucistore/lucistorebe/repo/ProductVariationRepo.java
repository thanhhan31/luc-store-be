package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductVariation;

@Repository
public interface ProductVariationRepo extends JpaRepository<ProductVariation, Long> {
	
	//List<ProductVariation> findByProductAndVariationName(Product p, String name);
	
}
