package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.Order_;
import com.lucistore.lucistorebe.repo.custom.OrderRepoCustom;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

@Repository
public class OrderRepoCustomImpl implements OrderRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	public boolean paymentConfirm(Long idOrder) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	/*CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    	Root<Order> root = cq.from(Order.class);
    	cq.select(root).where(cb.equal(root.get(Order_.id), idOrder));
    	
    	Order o = em.createQuery(cq)
    			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
    			.setHint("javax.persistence.query.timeout", 0)
    			.getSingleResult();
    	
    	if (o.getStatus().equals(EOrderStatus.WAIT_FOR_PAYMENT)) {
    		
    	}*/
    	
    	
    	
    	
    	CriteriaUpdate<Order> cu = cb.createCriteriaUpdate(Order.class);
    	Root<Order> root = cu.from(Order.class);
    	
		cu.set(Order_.status, EOrderStatus.WAIT_FOR_CONFIRM)
			.where(cb.and(
					cb.equal(root.get(Order_.id), idOrder),
					cb.equal(root.get(Order_.status), EOrderStatus.WAIT_FOR_PAYMENT)
				)
			);
		return em.createQuery(cu).executeUpdate() == 1;
    	
    	///return false;
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
