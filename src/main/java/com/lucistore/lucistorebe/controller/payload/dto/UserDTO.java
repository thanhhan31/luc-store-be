package com.lucistore.lucistorebe.controller.payload.dto;

import com.lucistore.lucistorebe.entity.user.UserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDTO {
	private Long id;
	private String fullname;
	private String username;
	private String email;
	private String phone;
	private UserRole role;
	private EUserStatus status;
}
