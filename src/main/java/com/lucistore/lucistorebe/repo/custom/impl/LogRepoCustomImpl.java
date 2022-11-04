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
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.entity.Log_;
import com.lucistore.lucistorebe.entity.user.User_;
import com.lucistore.lucistorebe.repo.custom.LogRepoCustom;
import com.lucistore.lucistorebe.utility.ELogType;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

@Repository
public class LogRepoCustomImpl implements LogRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Log> search(Long idUser, Date beginDate, Date endDate, ELogType logType, String searchContent, PageWithJpaSort page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Log> cq = cb.createQuery(Log.class);
		Root<Log> root = cq.from(Log.class);

		List<Predicate> filters = new ArrayList<>();

		if (idUser != null) {
			filters.add(cb.equal(root.get(Log_.user).get(User_.id), idUser));
		}
		if (beginDate != null && endDate != null) {
			filters.add(cb.between(root.get(Log_.date), beginDate, endDate));
		}
		if (logType != null) {
			filters.add(cb.equal(root.get(Log_.logType), logType));
		}
		if (searchContent != null) {
			filters.add(cb.like(root.get(Log_.content), "%" + searchContent + "%"));
		}

		if (page != null) {
			cq.orderBy(QueryUtils.toOrders(page.getSort(), root, cb));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(root).where(filter);
		TypedQuery<Log> query = em.createQuery(cq);

		if (page != null) {
			query.setFirstResult(page.getPageNumber() * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		return query.getResultList();
	}

	@Override
	public Long searchCount(Long idUser, Date beginDate, Date endDate, ELogType logType, String searchContent) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Log> root = cq.from(Log.class);
		
		List<Predicate> filters = new ArrayList<>();
		if (idUser != null) {
			filters.add(cb.equal(root.get(Log_.user).get(User_.id), idUser));
		}
		if (beginDate != null && endDate != null) {
			filters.add(cb.between(root.get(Log_.date), beginDate, endDate));
		}
		if (logType != null) {
			filters.add(cb.equal(root.get(Log_.logType), logType));
		}
		if (searchContent != null) {
			filters.add(cb.like(root.get(Log_.content), "%" + searchContent + "%"));
		}

		Predicate filter = cb.and(filters.toArray(new Predicate[0]));
		cq.select(cb.count(root)).where(filter);
		return em.createQuery(cq).getSingleResult();
	}
}
