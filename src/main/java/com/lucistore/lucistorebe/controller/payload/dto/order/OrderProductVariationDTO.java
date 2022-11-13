package com.lucistore.lucistorebe.controller.payload.dto.order;

import com.lucistore.lucistorebe.utility.EProductVariationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderProductVariationDTO {
	private Long id;
	private OrderProductGeneralDetailDTO product;
	private String variationName;
	private String tier;
	private Long price;
	private Long availableQuantity;
	private Integer discount;
	private EProductVariationStatus status;
}
