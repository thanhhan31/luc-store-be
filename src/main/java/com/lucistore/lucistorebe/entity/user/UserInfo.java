package com.lucistore.lucistorebe.entity.user;

import com.lucistore.lucistorebe.utility.EUserStatus;

public interface UserInfo {
	String getUsername();
	String getPassword();
	UserRole getRole();
	EUserStatus getStatus();
	boolean isActive();
}
