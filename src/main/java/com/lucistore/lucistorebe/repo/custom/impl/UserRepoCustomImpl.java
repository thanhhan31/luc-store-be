package com.lucistore.lucistorebe.repo.custom.impl;

import java.util.ArrayList;
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

import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.UserRole_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.repo.custom.UserRepoCustom;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

@Repository
public class UserRepoCustomImpl implements UserRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<User> searchAdmin(String searchFullname, String searchUsername, String searchEmail,
			String searchPhone, EUserRole role, EUserStatus status, PageWithJpaSort page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);

		List<Predicate> filters = new ArrayList<>();
		
		if (StringUtils.isNotEmpty(searchFullname)) {
			filters.add(cb.like(root.get(User_.fullname), "%" + searchFullname + "%"));
		}
		if (StringUtils.isNotEmpty(searchUsername)) {
			filters.add(cb.like(root.get(User_.username), "%" + searchUsername + "%"));
		}
		if (StringUtils.isNotEmpty(searchEmail)) {
			filters.add(cb.like(root.get(User_.email), "%" + searchEmail + "%"));
		}
		if (StringUtils.isNotEmpty(searchPhone)) {
			filters.add(cb.like(root.get(User_.phone), "%" + searchPhone + "%"));
		}
		
		if (role != null) {
			filters.add(cb.equal(root.get(User_.role).get(UserRole_.name), role.toString()));
		}
		else {
			
			filters.add(
					cb.or(
						cb.equal(root.get(User_.role).get(UserRole_.name), EUserRole.ADMIN.toString()), 
						cb.equal(root.get(User_.role).get(UserRole_.name), EUserRole.SALE_ADMIN.toString())
					)
				);
		}
		
		if (status != null) {
			filters.add(cb.equal(root.get(User_.status), status));
		}

		if (page != null) {
			cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(root).where(filter);
		TypedQuery<User> query = em.createQuery(cq);

		if (page != null) {
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		return query.getResultList();
	}

	@Override
	public Long searchAdminCount(String searchFullname, String searchUsername, String searchEmail,
			String searchPhone, EUserRole role, EUserStatus status) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<User> root = cq.from(User.class);
		
		List<Predicate> filters = new ArrayList<>();

		if (StringUtils.isNotEmpty(searchFullname)) {
			filters.add(cb.like(root.get(User_.fullname), "%" + searchFullname + "%"));
		}
		if (StringUtils.isNotEmpty(searchUsername)) {
			filters.add(cb.like(root.get(User_.username), "%" + searchUsername + "%"));
		}
		if (StringUtils.isNotEmpty(searchEmail)) {
			filters.add(cb.like(root.get(User_.email), "%" + searchEmail + "%"));
		}
		if (StringUtils.isNotEmpty(searchPhone)) {
			filters.add(cb.like(root.get(User_.phone), "%" + searchPhone + "%"));
		}

		if (role != null) {
			filters.add(cb.equal(root.get(User_.role).get(UserRole_.name), role.toString()));
		}
		else {
			
			filters.add(
					cb.or(
						cb.equal(root.get(User_.role).get(UserRole_.name), EUserRole.ADMIN.toString()), 
						cb.equal(root.get(User_.role).get(UserRole_.name), EUserRole.SALE_ADMIN.toString())
					)
				);
		}
		
		if (status != null) {
			filters.add(cb.equal(root.get(User_.status), status));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(cb.count(root)).where(filter);
		return em.createQuery(cq).getSingleResult();
	}
}
