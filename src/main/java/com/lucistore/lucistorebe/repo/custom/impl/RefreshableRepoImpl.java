package com.lucistore.lucistorebe.repo.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lucistore.lucistorebe.repo.custom.RefreshableRepo;

@Repository
public class RefreshableRepoImpl<T> implements RefreshableRepo<T> {
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	@Override
	public void refresh(T t) {
		em.refresh(t);
	}
}
