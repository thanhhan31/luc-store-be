package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.Order_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer_;
import com.lucistore.lucistorebe.repo.custom.OrderRepoCustom;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

@Repository
public class OrderRepoCustomImpl implements OrderRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public boolean isBuyerHavePendingOrder(Long idBuyer) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
    	
    	Root<Order> root = cq.from(Order.class);
    	
    	cq.select(cb.literal(1));
    	cq.where(
			cb.and(
	    			cb.notEqual(root.get(Order_.status), EOrderStatus.COMPLETED), 
	    			cb.notEqual(root.get(Order_.status), EOrderStatus.CANCELLED),
	    			cb.equal(root.get(Order_.buyer).get(Buyer_.id), idBuyer)
	    		)
		);
    	
    	try {
    		em.createQuery(cq).setMaxResults(1).getSingleResult();
    		return true;
    	}
    	catch (NoResultException e) {
			return false;
		}
	}
	
	@Override
	public List<Order> search(/* filter */ PageWithJpaSort page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> root = cq.from(Order.class);

		List<Predicate> filters = new ArrayList<>();
		// if (!idsCategory.isEmpty()) {
		// 	filters.add(root.get(Order_.category).get(OrderCategory_.id).in(idsCategory));
		// }
		// if (searchName != null) {
		// 	filters.add(cb.like(root.get(Order_.name), "%" + searchName + "%"));
		// }
		// if (searchDescription != null) {
		// 	filters.add(cb.like(root.get(Order_.description), "%" + searchDescription + "%"));
		// }
		// if (status != null) {
		// 	filters.add(cb.equal(root.get(Order_.status), status));
		// }
		// if (minPrice != null) {
		// 	filters.add(cb.greaterThanOrEqualTo(root.get(Order_.minPrice), minPrice));
		// }
		// if (maxPrice != null) {
		// 	filters.add(cb.lessThanOrEqualTo(root.get(Order_.maxPrice), maxPrice));
		// }

		if (page != null) {
			cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(root).where(filter);
		TypedQuery<Order> query = em.createQuery(cq);

		if (page != null) {
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		return query.getResultList();
	}

	@Override
	public Long searchCount(/* filter */) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Order> root = cq.from(Order.class);
		
		List<Predicate> filters = new ArrayList<>();
		// if (!idsCategory.isEmpty()) {
		// 	filters.add(root.get(Order_.category).get(OrderCategory_.id).in(idsCategory));
		// }
		// if (searchName != null) {
		// 	filters.add(cb.like(root.get(Order_.name), "%" + searchName + "%"));
		// }
		// if (searchDescription != null) {
		// 	filters.add(cb.like(root.get(Order_.description), "%" + searchDescription + "%"));
		// }
		// if (status != null) {
		// 	filters.add(cb.equal(root.get(Order_.status), status));
		// }
		// if (minPrice != null) {
		// 	filters.add(cb.greaterThanOrEqualTo(root.get(Order_.minPrice), minPrice));
		// }
		// if (maxPrice != null) {
		// 	filters.add(cb.lessThanOrEqualTo(root.get(Order_.maxPrice), maxPrice));
		// }

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(cb.count(root)).where(filter);
		return em.createQuery(cq).getSingleResult();
	}
}
