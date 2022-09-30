package com.lucistore.lucistorebe.entity.user;

public interface UserInfo {
	String getUsername();
	String getPassword();
	UserRole getRole();
	boolean isActive();
}
