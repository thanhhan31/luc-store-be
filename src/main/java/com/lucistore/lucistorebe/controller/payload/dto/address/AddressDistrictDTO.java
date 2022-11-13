package com.lucistore.lucistorebe.controller.payload.dto.address;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AddressDistrictDTO {
	private Long id;
	private List<AddressWardDTO> provinceCity;
	private String name;
}
