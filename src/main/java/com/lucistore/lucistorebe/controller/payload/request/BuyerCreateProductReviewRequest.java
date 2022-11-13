package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BuyerCreateProductReviewRequest {
	@NotNull
	private Long idOrder;
	
	@NotNull
	private Long idProductVariation;
	
	@NotNull
	@Min(1) @Max(5)
	private Integer point;
	
	@NotBlank
	private String content;
}
