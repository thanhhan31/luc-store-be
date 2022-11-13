package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.repo.custom.ProductRepoCustom;
import com.lucistore.lucistorebe.repo.custom.RefreshableRepo;
import com.lucistore.lucistorebe.utility.EProductStatus;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, ProductRepoCustom, RefreshableRepo<Product> {
	@Transactional
	@Modifying
    @Query(value = "UPDATE product SET nvisit = nvisit + 1 WHERE id = ?1", nativeQuery = true)
    int updateVisitCount(Long id);
	
	List<Product> findTop10ByStatusOrderByMaxDiscountDesc(EProductStatus status);
	List<Product> findTop10ByStatusOrderByCreatedDateDesc(EProductStatus status);
	List<Product> findTop10ByStatusOrderByNvisitDesc(EProductStatus status);
	List<Product> findTop10ByStatusOrderByNsoldDesc(EProductStatus status);
	
	//List<Product> findByName(String name);
}
