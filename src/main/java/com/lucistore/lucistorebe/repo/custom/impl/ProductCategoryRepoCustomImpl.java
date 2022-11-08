package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.repo.custom.ProductCategoryRepoCustom;

@Repository
public class ProductCategoryRepoCustomImpl implements ProductCategoryRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	String sql = "WITH RECURSIVE cte as (\r\n"
			+ "	SELECT *\r\n"
			+ "    FROM product_category\r\n"
			+ "    WHERE name LIKE ?1\r\n"
			+ "    \r\n"
			+ "    UNION ALL\r\n"
			+ "    \r\n"
			+ "    SELECT product_category.*\r\n"
			+ "    FROM product_category, cte\r\n"
			+ "    WHERE product_category.id_parent = cte.id AND product_category.name REGEXP ?2\r\n"
			+ ")\r\n"
			+ "SELECT * FROM cte;";
	
	@Override
	public Long tmp(List<String> names) {
		
		Query q = em.createNativeQuery(sql, ProductCategory.class);
		q.setParameter(1, names.get(0));
		
		String regex = "";
		for (int i = 1; i < names.size(); i++) {
			regex += "^" + names.get(i) + "$|";
		}
		regex = regex.substring(0, regex.length() - 1);
		q.setParameter(2, regex);

		List<ProductCategory> l = (List<ProductCategory>)q.getResultList();
		if (!l.isEmpty()) {
			return l.get(l.size() - 1).getId();
		}
		else {
			return -1L;
		}
	}
}
