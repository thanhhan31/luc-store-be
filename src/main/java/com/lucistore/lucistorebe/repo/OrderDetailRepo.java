package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.OrderDetail;
import com.lucistore.lucistorebe.entity.order.OrderDetailPK;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, OrderDetailPK> {
	
	boolean existsByOrderAndReviewed(Order order, boolean reviewed);
	
}
