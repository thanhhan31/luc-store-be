package com.lucistore.lucistorebe.entity.user;

import com.lucistore.lucistorebe.utility.ERole;

public interface UserInfo {
	String getUsername();
	String getPassword();
	ERole getRole();
	boolean isActive();
}
