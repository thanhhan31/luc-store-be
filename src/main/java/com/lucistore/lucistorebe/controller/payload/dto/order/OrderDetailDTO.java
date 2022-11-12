package com.lucistore.lucistorebe.controller.payload.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderDetailDTO {
	
	private OrderProductVariationDTO productVariation;
	
	private Long quantity;
	
	private Long unitPrice;
	
	private Boolean reviewed;
	
}
