package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.function.TriFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lucistore.lucistorebe.utility.filter.PagingInfo;

public class SearchRepo<T, V> {
	private final Class<T> cls;
	
	public SearchRepo(Class<T> cls) {
		this.cls = cls;
	}

	public Page<T> search(V filter, PagingInfo pagingInfo,
			TriFunction<V, CriteriaBuilder, Root<T>, Predicate> createFilters,
			TriFunction<CriteriaBuilder, Root<T>, PagingInfo, List<Order>> createSort,
			EntityManager em) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(cls);

		Root<T> root = cq.from(cls);
		
		cq.select(root);
		cq.where(createFilters.apply(filter, cb, root));
		if (pagingInfo.getSortBy() != null)
			cq.orderBy(createSort.apply(cb, root, pagingInfo));
		
		Pageable page = PageRequest.of(pagingInfo.getCurrentPage(), pagingInfo.getSize());
		List<T> content = em.createQuery(cq)
				.setFirstResult(page.getPageNumber() * page.getPageSize())
				.setMaxResults(page.getPageSize())
				.getResultList();
		
		return new PageImpl<>(
			content, 
			page, 
			countResultSize(cb, filter, createFilters, em)
		);
	}
	
	private int countResultSize(CriteriaBuilder cb, V filter,
			TriFunction<V, CriteriaBuilder, Root<T>, Predicate> createFilters,
			EntityManager em) {
		
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> root = cq.from(cls);
		cq.select(cb.count(root));
		cq.where(createFilters.apply(filter, cb, root));
		return em.createQuery(cq).getSingleResult().intValue();
	}
}
