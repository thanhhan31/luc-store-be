package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductReviewDTO {
	private Long id;
	private Long idBuyer;
	private String buyerUsername;

	private List<ProductReviewImageDTO> images;
	private Integer point;
	private String content;
	private Date time;
}
