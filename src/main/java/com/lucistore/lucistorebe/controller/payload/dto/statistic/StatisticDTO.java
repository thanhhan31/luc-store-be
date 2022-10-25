package com.lucistore.lucistorebe.controller.payload.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class StatisticDTO{
	private Integer timeUnit;
	private Long nProduct;
	private Long nBuyer;
	private Long income;
	private Long nOrder;
}
