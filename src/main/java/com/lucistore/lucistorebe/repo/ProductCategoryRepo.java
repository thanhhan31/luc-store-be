package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.ProductCategory;

@Repository
public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Long> {
	@Query(
			value = "WITH RECURSIVE cte as (\r\n"
					+ "	SELECT *\r\n"
					+ "		FROM product_category\r\n"
					+ "		WHERE id = :categoryId\r\n"
					+ "	UNION ALL\r\n"
					+ "    SELECT product_category.*\r\n"
					+ "		FROM product_category, cte\r\n"
					+ "		WHERE product_category.id = cte.id_parent)\r\n"
					+ "SELECT * FROM cte order by level asc;",
			nativeQuery = true)
	List<ProductCategory> findAncestry(@Param("categoryId") Long categoryId);
}
