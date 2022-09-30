package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDTO {
	private String fullname;
	private String username;
	private String email;
	private String phone;
	private EUserRole role;
	private EUserStatus status;
}
