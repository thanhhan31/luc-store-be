package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.OrderStatistic;
import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.OrderDetail;
import com.lucistore.lucistorebe.entity.order.OrderDetail_;
import com.lucistore.lucistorebe.entity.order.Order_;
import com.lucistore.lucistorebe.entity.product.ProductVariation_;
import com.lucistore.lucistorebe.entity.product.Product_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer_;
import com.lucistore.lucistorebe.repo.custom.StatisticRepoCustom;

@Repository
public class StatisticRepoCustomimpl implements StatisticRepoCustom{
    
    @PersistenceContext
	private EntityManager em;

    public List<OrderStatistic> getOrderStatistic(List<Long> idProducts,
            List<Long> idProductVariations,
            List<Long> idBuyers,
            List<Long> idAdmins,
            Date importDateFrom,
            Date importDateTo) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderStatistic> cq = cb.createQuery(OrderStatistic.class);
		Root<Order> root = cq.from(Order.class);

		ListJoin<Order, OrderDetail> joinOrderDetail = root.join(Order_.orderDetails, JoinType.LEFT);

		List<Predicate> filters = new ArrayList<>();


		if ( idProducts != null) {
			filters.add(joinOrderDetail.get(OrderDetail_.productVariation).get(ProductVariation_.product).get(Product_.id).in(idProducts));
		}
		if ( idProductVariations != null) {
			filters.add(joinOrderDetail.get(OrderDetail_.productVariation).get(ProductVariation_.id).in(idProductVariations));
		}
		if ( idBuyers != null) {
			filters.add(root.get(Order_.buyer).get(Buyer_.id).in(idBuyers));
		}
		if ( idAdmins != null) {
			filters.add(root.get(Order_.seller).get(User_.id).in(idAdmins));
		}
		if ( importDateFrom != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(Order_.createTime), importDateFrom));
		}
		if ( importDateTo != null) {
			filters.add(cb.lessThanOrEqualTo(root.get(Order_.createTime), importDateTo));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));

		cq.where(filter);

		cq.groupBy(root.get(Order_.id));

		cq.multiselect(cb.sum(joinOrderDetail.get(OrderDetail_.quantity)), joinOrderDetail.get(OrderDetail_.order));
		
		TypedQuery<OrderStatistic> query = em.createQuery(cq);
		return query.getResultList();
    }

    public List<OrderStatistic> statisticByMonth(List<Long> idProducts,
            List<Long> idProductVariations,
            List<Long> idBuyers,
            List<Long> idAdmins,
            int year) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrderStatistic> cq = cb.createQuery(OrderStatistic.class);
		Root<Order> root = cq.from(Order.class);

		ListJoin<Order, OrderDetail> joinOrderDetail = root.join(Order_.orderDetails, JoinType.LEFT);

		List<Predicate> filters = new ArrayList<>();


		if ( idProducts != null) {
			filters.add(joinOrderDetail.get(OrderDetail_.productVariation).get(ProductVariation_.product).get(Product_.id).in(idProducts));
		}
		if ( idProductVariations != null) {
			filters.add(joinOrderDetail.get(OrderDetail_.productVariation).get(ProductVariation_.id).in(idProductVariations));
		}
		if ( idBuyers != null) {
			filters.add(root.get(Order_.buyer).get(Buyer_.id).in(idBuyers));
		}
		if ( idAdmins != null) {
			filters.add(root.get(Order_.seller).get(User_.id).in(idAdmins));
		}
		if ( importDateFrom != null) {
			filters.add(cb.greaterThanOrEqualTo(root.get(Order_.createTime), importDateFrom));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));

		cq.where(filter);

		cq.groupBy(root.get(Order_.id));

		cq.multiselect(cb.sum(joinOrderDetail.get(OrderDetail_.quantity)), joinOrderDetail.get(OrderDetail_.order));
		
		TypedQuery<OrderStatistic> query = em.createQuery(cq);
		return query.getResultList();
    }

}
