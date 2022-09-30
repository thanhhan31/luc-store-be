package com.lucistore.lucistorebe.repo.custom;

import java.util.List;

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.Page;

public interface ProductRepoCustom {
	List<Product> search(Long idCategory, String searchName, String searchDescription, EProductStatus status,
			Long minPrice, Long maxPrice, Page page);
	Long searchCount(Long idCategory, String searchName, String searchDescription, EProductStatus status, Long minPrice, Long maxPrice);
	void refresh(Product product);
}
