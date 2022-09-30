package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.Product;
import com.lucistore.lucistorebe.entity.product.ProductCategory_;
import com.lucistore.lucistorebe.entity.product.Product_;
import com.lucistore.lucistorebe.repo.custom.ProductRepoCustom;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.Page;

@Repository
public class ProductRepoCustomImpl implements ProductRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Product> search(Long idCategory, String searchName, String searchDescription, EProductStatus status,
			Long minPrice, Long maxPrice, Page page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> cq = cb.createQuery(Product.class);
		Root<Product> root = cq.from(Product.class);

		List<Predicate> filters = new ArrayList<>();
		if (idCategory != null) {
			filters.add(cb.equal(root.get(Product_.category).get(ProductCategory_.id), idCategory));
		}
		if (searchName != null) {
			filters.add(cb.like(root.get(Product_.name), "%" + searchName + "%"));
		}
		if (searchDescription != null) {
			filters.add(cb.like(root.get(Product_.description), "%" + searchDescription + "%"));
		}
		if (status != null) {
			filters.add(cb.equal(root.get(Product_.status), status));
		}
		if (minPrice != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(Product_.minPrice), minPrice));
		}
		if (maxPrice != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(Product_.maxPrice), maxPrice));
		}

		if (page != null) {
			cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(root).where(filter);
		TypedQuery<Product> query = em.createQuery(cq);

		if (page != null) {
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		return query.getResultList();
	}

	@Override
	public Long searchCount(Long idCategory, String searchName, String searchDescription, EProductStatus status, Long minPrice, Long maxPrice) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Product> root = cq.from(Product.class);
		
		List<Predicate> filters = new ArrayList<>();
		if (idCategory != null) {
			filters.add(cb.equal(root.get(Product_.category).get(ProductCategory_.id), idCategory));
		}
		if (searchName != null) {
			filters.add(cb.like(root.get(Product_.name), "%" + searchName + "%"));
		}
		if (searchDescription != null) {
			filters.add(cb.like(root.get(Product_.description), "%" + searchDescription + "%"));
		}
		if (status != null) {
			filters.add(cb.equal(root.get(Product_.status), status));
		}
		if (minPrice != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(Product_.minPrice), minPrice));
		}
		if (maxPrice != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(Product_.maxPrice), maxPrice));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(cb.count(root)).where(filter);
		return em.createQuery(cq).getSingleResult();
	}
	
	@Transactional
	@Override
	public void refresh(Product product) {
		em.refresh(product);
	}
}
