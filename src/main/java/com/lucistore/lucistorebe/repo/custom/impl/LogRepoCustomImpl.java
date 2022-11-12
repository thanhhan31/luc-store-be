package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.function.TriFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.entity.Log_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.repo.custom.LogRepoCustom;
import com.lucistore.lucistorebe.utility.ModelSorting;
import com.lucistore.lucistorebe.utility.filter.LogFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

@Repository
public class LogRepoCustomImpl implements LogRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	private final SearchRepo<Log, LogFilter> searchRepo;
	private final TriFunction<LogFilter, CriteriaBuilder, Root<Log>, Predicate> createFiltersDelegate;
	private final TriFunction<CriteriaBuilder, Root<Log>, PagingInfo, List<Order>> createOrdersDelegate;
	
	public LogRepoCustomImpl() {
		searchRepo = new SearchRepo<>(Log.class);
		createFiltersDelegate = this::createFilters;
		createOrdersDelegate = 
			(cb, root, pagingInfo) -> 
				QueryUtils.toOrders(ModelSorting.getProductSort(pagingInfo.getSortBy(), pagingInfo.getSortDescending()), root, cb);
	}

	@Override
	public Page<Log> search(LogFilter filter, PagingInfo pagingInfo) {
		return searchRepo.search(filter, pagingInfo, createFiltersDelegate, createOrdersDelegate, em);
	}
	
	private Predicate createFilters(LogFilter filter, CriteriaBuilder cb, Root<Log> root) {
		List<Predicate> filters = new ArrayList<>();

		if (filter.getIdUser() != null) {
			filters.add(cb.equal(root.get(Log_.user).get(User_.id), filter.getIdUser()));
		}
		if (filter.getBeginDate() != null && filter.getEndDate() != null) {
			filters.add(cb.between(root.get(Log_.date), filter.getBeginDate(), filter.getEndDate()));
		}
		if (filter.getLogType() != null) {
			filters.add(cb.equal(root.get(Log_.logType), filter.getLogType()));
		}
		if (filter.getSearchContent() != null) {
			filters.add(cb.like(root.get(Log_.content), "%" + filter.getSearchContent() + "%"));
		}
		return cb.and(filters.toArray(new Predicate[0]));
	}
}
