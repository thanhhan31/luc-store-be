package com.lucistore.lucistorebe.repo.custom;

import java.util.List;

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

public interface ProductRepoCustom {
	List<Product> search(List<Long> idsCategory, String searchName, String searchDescription, EProductStatus status,
			Long minPrice, Long maxPrice, PageWithJpaSort page);
	Long searchCount(List<Long> idsCategory, String searchName, String searchDescription, EProductStatus status, Long minPrice, Long maxPrice);
	void refresh(Product product);
}
