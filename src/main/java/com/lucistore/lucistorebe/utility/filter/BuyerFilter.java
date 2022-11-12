package com.lucistore.lucistorebe.utility.filter;

import java.util.Date;

import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BuyerFilter {
	String searchFullname;
	String searchUsername;
	String searchEmail;
	String searchPhone;
	EUserStatus status;
	Date dob;
	EGender gender;
	Boolean emailConfirmed;
	Boolean phoneConfirmed;
	Date createdDate;
	String lastModifiedBy;
	
	public BuyerFilter(String searchFullname, String searchUsername, String searchEmail, String searchPhone,
			EUserStatus status, Date dob, EGender gender, Boolean emailConfirmed, Boolean phoneConfirmed,
			Date createdDate, String lastModifiedBy) {
		this.searchFullname = searchFullname;
		this.searchUsername = searchUsername;
		this.searchEmail = searchEmail;
		this.searchPhone = searchPhone;
		this.status = status;
		this.dob = dob;
		this.gender = gender;
		this.emailConfirmed = emailConfirmed;
		this.phoneConfirmed = phoneConfirmed;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
	}
}
