package com.lucistore.lucistorebe.repo.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.product.ProductReview;
import com.lucistore.lucistorebe.repo.custom.ProductReviewRepoCustom;

@Repository
public class ProductReviewRepoCustomImpl implements ProductReviewRepoCustom {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void refresh(ProductReview productReview) {
		em.refresh(productReview);
	}
}
