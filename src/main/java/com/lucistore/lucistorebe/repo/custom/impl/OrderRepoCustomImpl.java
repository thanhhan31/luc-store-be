package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.Order_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer_;
import com.lucistore.lucistorebe.repo.custom.OrderRepoCustom;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.ModelSorting;
import com.lucistore.lucistorebe.utility.filter.OrderFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

@Repository
public class OrderRepoCustomImpl implements OrderRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	private final SearchRepo<Order, OrderFilter> searchRepo;
	private final TriFunction<OrderFilter, CriteriaBuilder, Root<Order>, Predicate> createFiltersDelegate;
	private final TriFunction<CriteriaBuilder, Root<Order>, PagingInfo, List<javax.persistence.criteria.Order>> createOrdersDelegate;
	
	public OrderRepoCustomImpl() {
		searchRepo = new SearchRepo<>(Order.class);
		createFiltersDelegate = this::createFilters;
		createOrdersDelegate = 
			(cb, root, pagingInfo) -> 
				QueryUtils.toOrders(ModelSorting.getOrderSort(pagingInfo.getSortBy(), pagingInfo.getSortDescending()), root, cb);
	}
	
	@Override
	public Page<Order> search(OrderFilter filter, PagingInfo pagingInfo) {
		return searchRepo.search(filter, pagingInfo, createFiltersDelegate, createOrdersDelegate, em);
	}
	
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
	
	private Predicate createFilters(OrderFilter filter, CriteriaBuilder cb, Root<Order> root) {
		List<Predicate> filters = new ArrayList<>();
		
		if (filter.getIdBuyer() != null) {
			filters.add(cb.equal(root.get(Order_.buyer).get(Buyer_.id), filter.getIdBuyer()));
		}
		if (filter.getIdSeller() != null) {
			filters.add(cb.equal(root.get(Order_.seller).get(User_.id), filter.getIdSeller()));
		}
		if (filter.getIdDeliveryAddress() != null) {
			filters.add(cb.equal(root.get(Order_.deliveryAddress).get(BuyerDeliveryAddress_.id), filter.getIdDeliveryAddress()));
		}
		if (filter.getCreateTime() != null) {
			filters.add(
				cb.equal(
						cb.function("DATE", Date.class, root.get(Order_.createTime)), 
						DateUtils.truncate(filter.getCreateTime(), Calendar.DATE)
				)
			);
		}
		if (filter.getStatus() != null) {
			filters.add(cb.equal(root.get(Order_.status), filter.getStatus()));
		}
		if (filter.getPaymentMethod() != null) {
			filters.add(cb.equal(root.get(Order_.paymentMethod), filter.getPaymentMethod()));
		}
		
		return cb.and(filters.toArray(new Predicate[0]));
	}
}
