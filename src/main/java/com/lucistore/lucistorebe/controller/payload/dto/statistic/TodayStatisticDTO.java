package com.lucistore.lucistorebe.controller.payload.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TodayStatisticDTO{
	private Long newProduct;
	private Long newBuyer;
	private Long income;
	private Long newOrder;

	public TodayStatisticDTO(Long income, Long newOrder) {
		this.income = income != null ? income : 0;
		this.newOrder = newOrder;
	}
}
