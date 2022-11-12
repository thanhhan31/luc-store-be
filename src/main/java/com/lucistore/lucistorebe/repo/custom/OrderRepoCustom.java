package com.lucistore.lucistorebe.repo.custom;

import org.springframework.data.domain.Page;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.utility.filter.OrderFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

public interface OrderRepoCustom {
	
	Page<Order> search(OrderFilter filter, PagingInfo pagingInfo);
	
	boolean isBuyerHavePendingOrder(Long idBuyer);
	
}