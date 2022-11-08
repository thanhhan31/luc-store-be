package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.repo.custom.ProductCategoryRepoCustom;

@Repository
public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Long>, ProductCategoryRepoCustom {
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
	
	@Query(value = "SELECT pc FROM ProductCategory pc WHERE pc.name = ?1 AND pc.level = ?2") //select * from product_category where product_category.id_parent is null
	ProductCategory findByNameAndLevel(String name, Integer level);
	
	@Query(value = "SELECT pc FROM ProductCategory pc WHERE pc.parent IS NULL") //select * from product_category where product_category.id_parent is null
	List<ProductCategory> findAllRootCategories();
	
	@Query(value = "SELECT pc FROM ProductCategory pc WHERE pc.parent IS NULL AND pc.status <> com.lucistore.lucistorebe.utility.EProductCategoryStatus.BANNED")
	List<ProductCategory> buyerFindAllRootCategories();
}
