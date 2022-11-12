package com.lucistore.lucistorebe.repo.custom;

import org.springframework.data.domain.Page;

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.ProductFilter;

public interface ProductRepoCustom {
	
	Page<Product> search(ProductFilter filter, PagingInfo page);
	
}
