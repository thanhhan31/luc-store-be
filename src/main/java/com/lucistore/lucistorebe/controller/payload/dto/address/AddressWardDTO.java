package com.lucistore.lucistorebe.controller.payload.dto.address;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressWardDTO {
	private Long id;
	private AddressDistrictDTO district;
	private String name;
}
