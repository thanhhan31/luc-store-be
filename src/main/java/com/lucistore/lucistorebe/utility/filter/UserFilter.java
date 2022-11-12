package com.lucistore.lucistorebe.utility.filter;

import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserFilter {
	String searchFullname;
	String searchUsername;
	String searchEmail;
	String searchPhone;
	EUserRole role;
	EUserStatus status;
	
	public UserFilter(String searchFullname, String searchUsername, String searchEmail, String searchPhone,
			EUserRole role, EUserStatus status) {
		this.searchFullname = searchFullname;
		this.searchUsername = searchUsername;
		this.searchEmail = searchEmail;
		this.searchPhone = searchPhone;
		this.role = role;
		this.status = status;
	}
}
