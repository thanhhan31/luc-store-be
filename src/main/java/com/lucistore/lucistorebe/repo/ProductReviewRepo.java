package com.lucistore.lucistorebe.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.ProductReview;
import com.lucistore.lucistorebe.repo.custom.ProductReviewRepoCustom;

@Repository
public interface ProductReviewRepo extends JpaRepository<ProductReview, Long>, ProductReviewRepoCustom {
	@Query("SELECT pr FROM ProductReview pr WHERE pr.productVariation.product.id = ?1")
	Page<ProductReview> findByIdProduct(Long idProduct, Pageable pageable);
	
}
