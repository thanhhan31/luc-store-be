package com.lucistore.lucistorebe.controller.payload.dto.statistic;

import com.lucistore.lucistorebe.entity.order.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class OrderStatistic{
	private Long nProduct;
	private Order order;
}
