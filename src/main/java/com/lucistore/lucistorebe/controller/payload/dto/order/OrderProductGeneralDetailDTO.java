package com.lucistore.lucistorebe.controller.payload.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderProductGeneralDetailDTO {
	private Long id;
	private Long idCategory;
	private String name;
	private String avatar;
}
