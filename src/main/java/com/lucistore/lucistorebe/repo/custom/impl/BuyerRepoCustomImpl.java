package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer_;
import com.lucistore.lucistorebe.repo.custom.BuyerRepoCustom;
import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.Page;

@Repository
public class BuyerRepoCustomImpl implements BuyerRepoCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Buyer> searchBuyer(String searchFullname, String searchUsername, String searchEmail, String searchPhone, 
			EUserStatus status, Date dob, EGender gender, Boolean emailConfirmed, Boolean phoneConfirmed,
			Date createdDate, String lastModifiedBy, Page page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Buyer> cq = cb.createQuery(Buyer.class);
		Root<Buyer> root = cq.from(Buyer.class);

		List<Predicate> filters = new ArrayList<>();
		
		if (StringUtils.isNotEmpty(searchFullname)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.fullname), "%" + searchFullname + "%"));
		}
		if (StringUtils.isNotEmpty(searchUsername)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.username), "%" + searchUsername + "%"));
		}
		if (StringUtils.isNotEmpty(searchEmail)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.email), "%" + searchEmail + "%"));
		}
		if (StringUtils.isNotEmpty(searchPhone)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.phone), "%" + searchPhone + "%"));
		}
		if (StringUtils.isNotEmpty(lastModifiedBy)) {
			filters.add(cb.like(root.get(Buyer_.lastModifiedBy), "%" + lastModifiedBy + "%"));
		}
		
		if (status != null) {
			filters.add(cb.equal(root.get(Buyer_.user).get(User_.status), status));
		}
		
		if (dob != null) {
			filters.add(cb.equal(root.get(Buyer_.dob), dob));
		}
		if (gender != null) {
			filters.add(cb.equal(root.get(Buyer_.gender), gender));
		}
		if (emailConfirmed != null) {
			filters.add(cb.equal(root.get(Buyer_.emailConfirmed), emailConfirmed));
		}
		if (phoneConfirmed != null) {
			filters.add(cb.equal(root.get(Buyer_.phoneConfirmed), phoneConfirmed));
		}
		if (createdDate != null) {
			filters.add(cb.equal(root.get(Buyer_.createdDate), createdDate));
		}
		
		filters.add(cb.equal(root.get(Buyer_.user).get(User_.role), EUserRole.BUYER));

		if (page != null) {
			cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(root).where(filter);
		TypedQuery<Buyer> query = em.createQuery(cq);

		if (page != null) {
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		return query.getResultList();
	}

	@Override
	public Long searchBuyerCount(String searchFullname, String searchUsername, String searchEmail, String searchPhone,
			EUserStatus status, Date dob, EGender gender, Boolean emailConfirmed, Boolean phoneConfirmed,
			Date createdDate, String lastModifiedBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Buyer> root = cq.from(Buyer.class);
		
		List<Predicate> filters = new ArrayList<>();

		if (StringUtils.isNotEmpty(searchFullname)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.fullname), "%" + searchFullname + "%"));
		}
		if (StringUtils.isNotEmpty(searchUsername)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.username), "%" + searchUsername + "%"));
		}
		if (StringUtils.isNotEmpty(searchEmail)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.email), "%" + searchEmail + "%"));
		}
		if (StringUtils.isNotEmpty(searchPhone)) {
			filters.add(cb.like(root.get(Buyer_.user).get(User_.phone), "%" + searchPhone + "%"));
		}
		if (StringUtils.isNotEmpty(lastModifiedBy)) {
			filters.add(cb.like(root.get(Buyer_.lastModifiedBy), "%" + lastModifiedBy + "%"));
		}
		
		if (status != null) {
			filters.add(cb.equal(root.get(Buyer_.user).get(User_.status), status));
		}
		
		if (dob != null) {
			filters.add(cb.equal(root.get(Buyer_.dob), dob));
		}
		if (gender != null) {
			filters.add(cb.equal(root.get(Buyer_.gender), gender));
		}
		if (emailConfirmed != null) {
			filters.add(cb.equal(root.get(Buyer_.emailConfirmed), emailConfirmed));
		}
		if (phoneConfirmed != null) {
			filters.add(cb.equal(root.get(Buyer_.phoneConfirmed), phoneConfirmed));
		}
		if (createdDate != null) {
			filters.add(cb.equal(root.get(Buyer_.createdDate), createdDate));
		}
		
		filters.add(cb.equal(root.get(Buyer_.user).get(User_.role), EUserRole.BUYER));

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(cb.count(root)).where(filter);
		return em.createQuery(cq).getSingleResult();
	}
}
