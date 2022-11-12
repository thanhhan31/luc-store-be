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
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.UserRole_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer_;
import com.lucistore.lucistorebe.repo.custom.BuyerRepoCustom;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.filter.BuyerFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

@Repository
public class BuyerRepoCustomImpl implements BuyerRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	private final SearchRepo<Buyer, BuyerFilter> searchRepo;
	private final TriFunction<BuyerFilter, CriteriaBuilder, Root<Buyer>, Predicate> createFiltersDelegate;
	private final TriFunction<CriteriaBuilder, Root<Buyer>, PagingInfo, List<Order>> createOrdersDelegate;
	
	public BuyerRepoCustomImpl() {
		searchRepo = new SearchRepo<>(Buyer.class);
		createFiltersDelegate = this::createFilters;
		createOrdersDelegate = (cb, root, pagingInfo) -> createSort(cb, root, pagingInfo.getSortBy(), pagingInfo.getSortDescending());
	}

	@Override
	public Page<Buyer> search(BuyerFilter filter, PagingInfo pagingInfo) {
		return searchRepo.search(filter, pagingInfo, createFiltersDelegate, createOrdersDelegate, em);
	}

	private Predicate createFilters(BuyerFilter filter, CriteriaBuilder cb, Root<Buyer> root) {
		List<Predicate> filters = new ArrayList<>();
		
		if (StringUtils.isNotEmpty(filter.getSearchFullname())) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.fullname), "%" + filter.getSearchFullname() + "%"));
		}
		if (StringUtils.isNotEmpty(filter.getSearchUsername())) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.username), "%" + filter.getSearchUsername() + "%"));
		}
		if (StringUtils.isNotEmpty(filter.getSearchEmail())) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.email), "%" + filter.getSearchEmail() + "%"));
		}
		if (StringUtils.isNotEmpty(filter.getSearchPhone())) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.phone), "%" + filter.getSearchPhone() + "%"));
		}
		if (StringUtils.isNotEmpty(filter.getLastModifiedBy())) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.lastModifiedBy), "%" + filter.getLastModifiedBy() + "%"));
		}
		
		if (filter.getStatus() != null) {
			filters.add(cb.equal(root.get(Buyer_.user).get(User_.status), filter.getStatus()));
		}
		
		if (filter.getDob() != null) {
			filters.add(cb.equal(root.get(Buyer_.dob), filter.getDob()));
		}
		if (filter.getGender() != null) {
			filters.add(cb.equal(root.get(Buyer_.gender), filter.getGender()));
		}
		if (filter.getEmailConfirmed() != null) {
			filters.add(cb.equal(root.get(Buyer_.emailConfirmed), filter.getEmailConfirmed()));
		}
		if (filter.getPhoneConfirmed() != null) {
			filters.add(cb.equal(root.get(Buyer_.phoneConfirmed), filter.getPhoneConfirmed()));
		}
		if (filter.getCreatedDate() != null) {
			filters.add(cb.equal(root.get(Buyer_.createdDate), filter.getCreatedDate()));
		}
		
		filters.add(cb.equal(root.get(Buyer_.user).get(User_.role).get(UserRole_.name), EUserRole.BUYER.toString()));
		return cb.and(filters.toArray(new Predicate[0]));
	}
	
	private List<Order> createSort(CriteriaBuilder cb, Root<Buyer> root, Integer sortBy, Boolean sortDescending) {
		List<Order> orders = new ArrayList<>();
		
		if (sortBy != null) {
			if (sortDescending == null || !sortDescending.booleanValue()) { //ASC
				if (sortBy >= 16) {
					orders.add(cb.asc(root.get(Buyer_.createdDate)));
					sortBy -= 16;
				}
				if (sortBy >= 8) {
					orders.add(cb.asc(root.get(Buyer_.user).get(User_.phone)));
					sortBy -= 8;
				}
				if (sortBy >= 4) {
					orders.add(cb.asc(root.get(Buyer_.user).get(User_.email)));
					sortBy -= 4;
				}
				if (sortBy >= 2) {
					orders.add(cb.asc(root.get(Buyer_.user).get(User_.username)));
					sortBy -= 2;
				}
				if (sortBy >= 1) {
					orders.add(cb.asc(root.get(Buyer_.user).get(User_.fullname)));
				}
			}
			else {	//DESC
				if (sortBy >= 16) {
					orders.add(cb.desc(root.get(Buyer_.createdDate)));
					sortBy -= 16;
				}
				if (sortBy >= 8) {
					orders.add(cb.desc(root.get(Buyer_.user).get(User_.phone)));
					sortBy -= 8;
				}
				if (sortBy >= 4) {
					orders.add(cb.desc(root.get(Buyer_.user).get(User_.email)));
					sortBy -= 4;
				}
				if (sortBy >= 2) {
					orders.add(cb.desc(root.get(Buyer_.user).get(User_.username)));
					sortBy -= 2;
				}
				if (sortBy >= 1) {
					orders.add(cb.desc(root.get(Buyer_.user).get(User_.fullname)));
				}
			}
		}
		return orders;
	}
}
