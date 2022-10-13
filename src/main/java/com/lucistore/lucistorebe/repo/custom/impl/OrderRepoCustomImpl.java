package com.lucistore.lucistorebe.repo.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.Order_;
import com.lucistore.lucistorebe.utility.EOrderStatus;

@Repository
public class OrderRepoCustomImpl {
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
	
}
