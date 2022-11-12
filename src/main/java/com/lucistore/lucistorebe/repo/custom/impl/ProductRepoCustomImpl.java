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

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductCategory_;
import com.lucistore.lucistorebe.entity.product.Product_;
import com.lucistore.lucistorebe.repo.custom.ProductRepoCustom;
import com.lucistore.lucistorebe.utility.ModelSorting;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.ProductFilter;

@Repository
public class ProductRepoCustomImpl implements ProductRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	private final SearchRepo<Product, ProductFilter> searchRepo;
	private final TriFunction<ProductFilter, CriteriaBuilder, Root<Product>, Predicate> createFiltersDelegate;
	private final TriFunction<CriteriaBuilder, Root<Product>, PagingInfo, List<Order>> createOrdersDelegate;
	
	public ProductRepoCustomImpl() {
		searchRepo = new SearchRepo<>(Product.class);
		createFiltersDelegate = this::createFilters;
		createOrdersDelegate = 
			(cb, root, pagingInfo) -> 
				QueryUtils.toOrders(ModelSorting.getProductSort(pagingInfo.getSortBy(), pagingInfo.getSortDescending()), root, cb);
	}

	@Override
	public Page<Product> search(ProductFilter filter, PagingInfo pagingInfo) {
		return searchRepo.search(filter, pagingInfo, createFiltersDelegate, createOrdersDelegate, em);
	}
	
	private Predicate createFilters(ProductFilter filter, CriteriaBuilder cb, Root<Product> root) {
		List<Predicate> filters = new ArrayList<>();
		
		if (!filter.getIdsCategory().isEmpty()) {
			filters.add(root.get(Product_.category).get(ProductCategory_.id).in(filter.getIdsCategory()));
		}
		if (filter.getSearchName() != null) {
			filters.add(cb.like(root.get(Product_.name), "%" + filter.getSearchName() + "%"));
		}
		if (filter.getSearchDescription() != null) {
			filters.add(cb.like(root.get(Product_.description), "%" + filter.getSearchDescription() + "%"));
		}
		if (filter.getStatus() != null) {
			filters.add(cb.equal(root.get(Product_.status), filter.getStatus()));
		}
		if (filter.getMinPrice() != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(Product_.minPrice), filter.getMinPrice()));
		}
		if (filter.getMaxPrice() != null) {
			filters.add(cb.lessThanOrEqualTo(root.get(Product_.maxPrice), filter.getMaxPrice()));
		}

		return cb.and(filters.toArray(new Predicate[0]));
	}
}
