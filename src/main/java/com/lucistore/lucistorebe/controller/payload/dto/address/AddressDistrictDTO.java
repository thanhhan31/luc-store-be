package com.lucistore.lucistorebe.controller.payload.dto.address;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressDistrictDTO {
	private Long id;
	private AddressProvinceCityDTO provinceCity;
	private String name;
}
