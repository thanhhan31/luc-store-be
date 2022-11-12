package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.StatisticDTO;
import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.OrderDetail;
import com.lucistore.lucistorebe.entity.order.OrderDetail_;
import com.lucistore.lucistorebe.entity.order.Order_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer_;
import com.lucistore.lucistorebe.repo.custom.StatisticRepoCustom;
import com.lucistore.lucistorebe.utility.EStatisticType;

@Repository
public class StatisticRepoCustomimpl implements StatisticRepoCustom{
    
    @PersistenceContext
	private EntityManager em;

    public List<StatisticDTO> statistic(
            List<Long> idBuyers,
            List<Long> idAdmins,
			Integer month,
			Integer quarter,
            Integer year,
			EStatisticType type) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<StatisticDTO> main = cb.createQuery(StatisticDTO.class);
		Root<Order> rootMain = main.from(Order.class);

		List<Predicate> filters = new ArrayList<>();


		if ( idBuyers != null) {
			filters.add(rootMain.get(Order_.buyer).get(Buyer_.id).in(idBuyers));
		}
		if ( idAdmins != null) {
			filters.add(rootMain.get(Order_.seller).get(User_.id).in(idAdmins));
		}
		if ( year != null) {
			filters.add(cb.equal(cb.function("YEAR", Integer.class, rootMain.get(Order_.createTime)), year));
		}
		if ( month != null) {
			filters.add(cb.equal(cb.function("MONTH", Integer.class, rootMain.get(Order_.createTime)), month));
		}
		if ( quarter != null) {
			filters.add(cb.equal(cb.function("QUARTER", Integer.class, rootMain.get(Order_.createTime)), quarter));
		}
	
		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		
		
		Subquery<Long> sub = main.subquery(Long.class);
		Root<OrderDetail> subRoot = sub.from(OrderDetail.class);
		sub.where(cb.equal(subRoot.get(OrderDetail_.order).get(Order_.id), rootMain.get(Order_.id)));
		sub.select(cb.sum(subRoot.get(OrderDetail_.quantity)));
		
		Expression<Integer> timeUnit;
		List<Expression<?>> timeGroup = new ArrayList<>();

		if( month != null || quarter != null) {
			timeUnit = cb.function("DAY", Integer.class, rootMain.get(Order_.createTime));
			timeGroup.add(cb.function("MONTH", Integer.class, rootMain.get(Order_.createTime)));
		} else {
			timeUnit = cb.function(type.name(), Integer.class, rootMain.get(Order_.createTime));
		}
		timeGroup.add(timeUnit);
		
		main.multiselect(timeUnit
				, cb.sum(sub) //quantity
				, cb.countDistinct(rootMain.get(Order_.buyer).get(Buyer_.id))
				, cb.sum(rootMain.get(Order_.payPrice))
				, cb.count(rootMain.get(Order_.id)));
		
		main.groupBy(timeGroup);
		main.where(filter);
		
		return em.createQuery(main).getResultList();
    }

}
