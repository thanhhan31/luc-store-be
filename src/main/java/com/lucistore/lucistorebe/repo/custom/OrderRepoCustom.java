package com.lucistore.lucistorebe.repo.custom;

import java.util.List;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

public interface OrderRepoCustom {
	List<Order> search(PageWithJpaSort page);
	Long searchCount();
	boolean isBuyerHavePendingOrder(Long idBuyer);
}