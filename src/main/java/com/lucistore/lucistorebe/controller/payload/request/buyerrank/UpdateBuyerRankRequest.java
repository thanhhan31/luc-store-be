package com.lucistore.lucistorebe.controller.payload.request.buyerrank;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateBuyerRankRequest {
	@NotNull @Min(0) @Max(100)
	private Double discountRate;
}
