package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.ProductInventory;
import com.lucistore.lucistorebe.entity.product.ProductInventory_;
import com.lucistore.lucistorebe.entity.product.ProductVariation_;
import com.lucistore.lucistorebe.entity.product.Product_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.repo.custom.ProductInventoryRepoCustom;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

@Repository
public class ProductInventoryRepoCustomImpl implements ProductInventoryRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<ProductInventory> search(Long idProduct, Long idProductVariation, Long idImporter, Date importTimeFrom , Date importTimeTo,
	PageWithJpaSort page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProductInventory> cq = cb.createQuery(ProductInventory.class);
		Root<ProductInventory> root = cq.from(ProductInventory.class);

		List<Predicate> filters = new ArrayList<>();
		if (idProduct != null) {
			filters.add(cb.equal(
					root.get(ProductInventory_.variation)
							.get(ProductVariation_.product)
							.get(Product_.id),
					idProduct));
		}

		if (idProductVariation != null) {
			filters.add(cb.equal(root.get(ProductInventory_.id), idProductVariation));
		}
		if (idImporter != null) {
			filters.add(cb.equal(root.get(ProductInventory_.importer).get(User_.id), idImporter));
		}
		if (importTimeFrom != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(ProductInventory_.importTime), importTimeFrom));
		}
		if (importTimeTo != null) {
			filters.add(cb.lessThanOrEqualTo(root.get(ProductInventory_.importTime), importTimeTo));
		}

		if (page != null) {
			cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(root).where(filter);
		TypedQuery<ProductInventory> query = em.createQuery(cq);

		if (page != null) {
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		return query.getResultList();
	}

	@Override
	public Long searchCount(Long idProduct, Long idProductVariation, Long idImporter, Date importTimeFrom, Date importTimeTo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ProductInventory> root = cq.from(ProductInventory.class);
		
		List<Predicate> filters = new ArrayList<>();

		if (idProduct != null) {
			filters.add(cb.equal(
					root.get(ProductInventory_.variation)
							.get(ProductVariation_.product)
							.get(Product_.id),
					idProduct));
		}

		if (idProductVariation != null) {
			filters.add(cb.equal(root.get(ProductInventory_.id), idProductVariation));
		}
		if (idImporter != null) {
			filters.add(cb.equal(root.get(ProductInventory_.importer).get(User_.id), idImporter));
		}
		if (importTimeFrom != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(ProductInventory_.importTime), importTimeFrom));
		}
		if (importTimeTo != null) {
			filters.add(cb.lessThanOrEqualTo(root.get(ProductInventory_.importTime), importTimeTo));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(cb.count(root)).where(filter);
		return em.createQuery(cq).getSingleResult();
	}
}
