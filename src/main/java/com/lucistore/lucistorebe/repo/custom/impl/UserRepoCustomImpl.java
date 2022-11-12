package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.UserRole_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.repo.custom.UserRepoCustom;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.ModelSorting;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.UserFilter;

@Repository
public class UserRepoCustomImpl implements UserRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	private final SearchRepo<User, UserFilter> searchRepo;
	private final TriFunction<UserFilter, CriteriaBuilder, Root<User>, Predicate> createFiltersDelegate;
	private final TriFunction<CriteriaBuilder, Root<User>, PagingInfo, List<Order>> createOrdersDelegate;
	
	public UserRepoCustomImpl() {
		searchRepo = new SearchRepo<>(User.class);
		createFiltersDelegate = this::createFilters;
		createOrdersDelegate = 
			(cb, root, pagingInfo) -> 
				QueryUtils.toOrders(ModelSorting.getProductSort(pagingInfo.getSortBy(), pagingInfo.getSortDescending()), root, cb);
	}
	
	@Override
	public Page<User> search(UserFilter filter, PagingInfo pagingInfo) {
		return searchRepo.search(filter, pagingInfo, createFiltersDelegate, createOrdersDelegate, em);
	}
	
	private Predicate createFilters(UserFilter filter, CriteriaBuilder cb, Root<User> root) {
		List<Predicate> filters = new ArrayList<>();
		
		if (StringUtils.isNotEmpty(filter.getSearchFullname())) {
			filters.add(cb.like(root.get(User_.fullname), "%" + filter.getSearchFullname() + "%"));
		}
		if (StringUtils.isNotEmpty(filter.getSearchUsername())) {
			filters.add(cb.like(root.get(User_.username), "%" + filter.getSearchUsername() + "%"));
		}
		if (StringUtils.isNotEmpty(filter.getSearchEmail())) {
			filters.add(cb.like(root.get(User_.email), "%" + filter.getSearchEmail() + "%"));
		}
		if (StringUtils.isNotEmpty(filter.getSearchPhone())) {
			filters.add(cb.like(root.get(User_.phone), "%" + filter.getSearchPhone() + "%"));
		}
		
		if (filter.getRole() != null) {
			filters.add(cb.equal(root.get(User_.role).get(UserRole_.name), filter.getRole().toString()));
		}
		else {
			filters.add(
					cb.or(
						cb.equal(root.get(User_.role).get(UserRole_.name), EUserRole.ADMIN.toString()), 
						cb.equal(root.get(User_.role).get(UserRole_.name), EUserRole.SALE_ADMIN.toString())
					)
				);
		}
		
		if (filter.getStatus() != null) {
			filters.add(cb.equal(root.get(User_.status), filter.getStatus()));
		}
		
		return cb.and(filters.toArray(new Predicate[0]));
	}
}
