package com.lucistore.lucistorebe.controller.payload.dto.address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AddressDistrictDTO {
	private Long id;
	private AddressProvinceCityDTO provinceCity;
	private String name;
}
