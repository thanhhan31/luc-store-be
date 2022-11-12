package com.lucistore.lucistorebe.repo.custom;

import org.springframework.data.domain.Page;

import com.lucistore.lucistorebe.entity.product.ProductInventory;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.ProductInventoryFilter;

public interface ProductInventoryRepoCustom {
	Page<ProductInventory> search(ProductInventoryFilter filter, PagingInfo pagingInfo);
}
