package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.utility.EGender;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerDTO extends UserDTO {
	private String avatar;
	private EGender gender;
}
