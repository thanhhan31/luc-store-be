package com.lucistore.lucistorebe.repo.custom;

import org.springframework.data.domain.Page;

import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.utility.filter.BuyerFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

public interface BuyerRepoCustom {

	Page<Buyer> search(BuyerFilter filter, PagingInfo pagingInfo);
	
}