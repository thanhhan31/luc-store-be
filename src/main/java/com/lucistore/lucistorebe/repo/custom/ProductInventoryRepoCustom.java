package com.lucistore.lucistorebe.repo.custom;

import java.util.Date;
import java.util.List;

import com.lucistore.lucistorebe.entity.product.ProductInventory;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

public interface ProductInventoryRepoCustom {
	List<ProductInventory> search(Long idProduct, Long idProductVariation, Long idImporter, Date importTimeFrom, Date importTimeTo, PageWithJpaSort page);
	Long searchCount(Long idProduct, Long idProductVariation, Long idImporter, Date importTimeFrom, Date importTimeTo);
}
