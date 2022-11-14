package com.lucistore.lucistorebe.controller.payload.dto.address.forward;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AddressProvinceCityDetailDTO {
	private Long id;
	private String name;
	private List<AddressDistrictDTO> districts; 
}
