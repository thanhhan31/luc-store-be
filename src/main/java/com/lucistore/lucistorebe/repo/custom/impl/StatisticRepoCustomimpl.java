package com.lucistore.lucistorebe.repo.custom.impl;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.StatisticDTO;
import com.lucistore.lucistorebe.controller.payload.dto.statistic.TodayStatisticDTO;
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
			filters.add(cb.equal(cb.function(EStatisticType.YEAR.name(), Integer.class, rootMain.get(Order_.createTime)), year));
		}
		if ( month != null) {
			filters.add(cb.equal(cb.function(EStatisticType.MONTH.name(), Integer.class, rootMain.get(Order_.createTime)), month));
		}
		if ( quarter != null) {
			filters.add(cb.equal(cb.function(EStatisticType.QUARTER.name(), Integer.class, rootMain.get(Order_.createTime)), quarter));
		}
	
		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		
		Subquery<Long> sub = main.subquery(Long.class);
		Root<OrderDetail> subRoot = sub.from(OrderDetail.class);
		sub.where(cb.equal(subRoot.get(OrderDetail_.order).get(Order_.id), rootMain.get(Order_.id)));
		sub.select(cb.sum(subRoot.get(OrderDetail_.quantity)));
		
		Expression<Integer> timeUnit;
		List<Expression<?>> timeGroup = new ArrayList<>();

		String timeUnitName = "";
		int numberOfTimeUnit = 0;

		if( month != null ) { // statistic by month, time unit is day
			timeUnit = cb.function(EStatisticType.DAY.name(), Integer.class, rootMain.get(Order_.createTime));
			timeUnitName = EStatisticType.DAY.name();

			YearMonth yearMonth = YearMonth.of(year, month);
			numberOfTimeUnit = yearMonth.lengthOfMonth(); // get number of day in month

		} else if ( quarter != null ) { // statistic by quarter, time unit is month
			timeUnit = cb.function(EStatisticType.MONTH.name(), Integer.class, rootMain.get(Order_.createTime));
			timeUnitName = EStatisticType.MONTH.name();
			numberOfTimeUnit = 3; // 3 month in a quarter
		} else{ // statistic by year, time unit is month or quarter (depend on type)
			timeUnit = cb.function(type.name(), Integer.class, rootMain.get(Order_.createTime));
			timeUnitName = type.name();

			if( type == EStatisticType.MONTH ) {
				numberOfTimeUnit = 12; // 12 month in a year
			} else if ( type == EStatisticType.QUARTER ) {
				numberOfTimeUnit = 4; // 4 quarter in a year
			}
		}
		timeGroup.add(timeUnit);
		
		main.multiselect(timeUnit
				, cb.selectCase().when(cb.isNull(cb.sum(sub)), 0L).otherwise(cb.sum(sub))
				, cb.countDistinct(rootMain.get(Order_.buyer).get(Buyer_.id))
				, cb.selectCase().when(cb.isNull(rootMain.get(Order_.payPrice)), 0L).otherwise(cb.sum(rootMain.get(Order_.payPrice))) // if payPrice is null, set it to 0
				, cb.count(rootMain.get(Order_.id)));
		
		main.groupBy(timeGroup);
		main.where(filter);
		
		var rs = em.createQuery(main).getResultList();
		List<StatisticDTO> result = new ArrayList<>();

		for(int i = 1; i <= numberOfTimeUnit; i++) { // fill missing time unit
			if(quarter != null){ // if statistic by quarter, time unit is month, need to convert numberOfTimeUnit to month
				// 3 * quarter - 2 = the first month of quarter
				// 3 * quarter - 2 + i - 1 = the i-th month of quarter
				result.add(new StatisticDTO(3 * quarter - 2 + i - 1, 0L, 0L, 0L, 0L));
			} else {
				result.add(new StatisticDTO(i, 0L, 0L, 0L, 0L));
			}
				
		}

		for(int i = 0; i < rs.size(); i++) { // set time unit that has data
			if(quarter != null){ // if statistic by quarter, config index to match month
				// 3 * quarter - 2 = the first month of quarter
				// 3 * quarter - 2 + i - 1 = the i-th month of quarter
				// i = i-th month + 3 - 3 * quarter 
				
				result.set(rs.get(i).getTimeUnit() + 3 - 3 * quarter - 1, rs.get(i));
			} else 
				result.set(rs.get(i).getTimeUnit() - 1, rs.get(i));
		}

		for( var item : result) { // set label for time unit
			item.setLabel(timeUnitName);
		}

		return result;
    }

    public TodayStatisticDTO todayStatistic() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> main = cb.createQuery(Tuple.class);
		Root<Order> rootMain = main.from(Order.class);

		List<Predicate> filters = new ArrayList<>();

		filters.add(cb.equal(cb.function("DATE", Integer.class, rootMain.get(Order_.createTime)), cb.currentDate()));
	
		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		
		main.multiselect(
				cb.selectCase().when(cb.isNull(cb.sum(rootMain.get(Order_.payPrice))), 0L)
						.otherwise(cb.sum(rootMain.get(Order_.payPrice))),
				cb.count(rootMain.get(Order_.id)));
		main.where(filter);
		Tuple rs = em.createQuery(main).getSingleResult();
		return new TodayStatisticDTO(rs.get(0, Long.class), rs.get(1, Long.class));
    }

}
