package com.lucistore.lucistorebe.repo.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.Order_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer_;
import com.lucistore.lucistorebe.repo.custom.OrderRepoCustom;
import com.lucistore.lucistorebe.utility.EOrderStatus;

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
}
