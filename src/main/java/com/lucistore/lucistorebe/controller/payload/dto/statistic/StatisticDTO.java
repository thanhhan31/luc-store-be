package com.lucistore.lucistorebe.controller.payload.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class StatisticDTO{
	private Long nProduct = 0L;
	private Long nBuyer = 0L;
	private Long income = 0L;
	private Long nOrder = 0L;
}
