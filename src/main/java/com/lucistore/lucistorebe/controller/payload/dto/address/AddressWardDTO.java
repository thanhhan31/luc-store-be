package com.lucistore.lucistorebe.controller.payload.dto.address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AddressWardDTO {
	private Long id;
	private AddressDistrictDTO district;
	private String name;
}
