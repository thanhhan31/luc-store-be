package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.function.TriFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.ProductInventory;
import com.lucistore.lucistorebe.entity.product.ProductInventory_;
import com.lucistore.lucistorebe.entity.product.ProductVariation_;
import com.lucistore.lucistorebe.entity.product.Product_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.repo.custom.ProductInventoryRepoCustom;
import com.lucistore.lucistorebe.utility.ModelSorting;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.ProductInventoryFilter;

@Repository
public class ProductInventoryRepoCustomImpl implements ProductInventoryRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	private final SearchRepo<ProductInventory, ProductInventoryFilter> searchRepo;
	private final TriFunction<ProductInventoryFilter, CriteriaBuilder, Root<ProductInventory>, Predicate> createFiltersDelegate;
	private final TriFunction<CriteriaBuilder, Root<ProductInventory>, PagingInfo, List<Order>> createOrdersDelegate;
	
	public ProductInventoryRepoCustomImpl() {
		searchRepo = new SearchRepo<>(ProductInventory.class);
		createFiltersDelegate = this::createFilters;
		createOrdersDelegate = 
			(cb, root, pagingInfo) -> 
				QueryUtils.toOrders(ModelSorting.getProductSort(pagingInfo.getSortBy(), pagingInfo.getSortDescending()), root, cb);
	}
	
	@Override
	public Page<ProductInventory> search(ProductInventoryFilter filter, PagingInfo pagingInfo) {
		return searchRepo.search(filter, pagingInfo, createFiltersDelegate, createOrdersDelegate, em);
	}

	private Predicate createFilters(ProductInventoryFilter filter, CriteriaBuilder cb, Root<ProductInventory> root) {
		List<Predicate> filters = new ArrayList<>();
		
		if (filter.getIdProduct() != null) {
			filters.add(
				cb.equal(
					root.get(ProductInventory_.variation).get(ProductVariation_.product).get(Product_.id),
					filter.getIdProduct()
				)
			);
		}
		if (filter.getIdProductVariation() != null) {
			filters.add(cb.equal(root.get(ProductInventory_.id), filter.getIdProductVariation()));
		}
		if (filter.getIdImporter() != null) {
			filters.add(cb.equal(root.get(ProductInventory_.importer).get(User_.id), filter.getIdImporter()));
		}
		if (filter.getImportTimeFrom() != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(ProductInventory_.importTime), filter.getImportTimeFrom()));
		}
		if (filter.getImportTimeTo() != null) {
			filters.add(cb.lessThanOrEqualTo(root.get(ProductInventory_.importTime), filter.getImportTimeTo()));
		}
		
		return cb.and(filters.toArray(new Predicate[0]));
	}
}
