package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.Date;

import com.lucistore.lucistorebe.entity.user.buyer.BuyerRank;
import com.lucistore.lucistorebe.utility.EGender;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerDTO extends UserDTO {
	private String avatar;
	private EGender gender;
	private Date dob;
	private Date createdDate;
	private Boolean emptyPassword;
	private BuyerRank rank;
	private Boolean canChangeUsername;
	private Boolean emailConfirmed;
	private Boolean phoneConfirmed;
}
