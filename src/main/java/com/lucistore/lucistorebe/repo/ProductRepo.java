package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.repo.custom.ProductRepoCustom;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, ProductRepoCustom {
	@Modifying
    @Query(value = "UPDATE product SET nvisit = nvisit + 1 WHERE id = ?1", nativeQuery = true)
    int updateVisitCount(Long id);
}
