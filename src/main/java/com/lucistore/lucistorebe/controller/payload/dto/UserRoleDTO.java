package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.List;

import com.lucistore.lucistorebe.utility.ERolePermission;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRoleDTO {
	private String name;
	private List<ERolePermission> permissions;
}
