package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucistore.lucistorebe.utility.EGender;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BuyerDTO extends UserDTO {
	private String avatar;
	private EGender gender;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	private Boolean emptyPassword;
	private BuyerRankDTO rank;
	private BuyerDeliveryAddressDTO defaultAddress;
	private Boolean canChangeUsername;
	private Boolean emailConfirmed;
	private Boolean phoneConfirmed;
	private Boolean canChangeEmail;
	private Long totalSpent;
	private Date createdDate;
}
