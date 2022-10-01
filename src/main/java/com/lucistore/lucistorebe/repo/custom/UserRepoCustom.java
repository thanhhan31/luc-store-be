package com.lucistore.lucistorebe.repo.custom;

import java.util.List;

import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.Page;

public interface UserRepoCustom {

	List<User> searchAdmin(String searchFullname, String searchUsername, String searchEmail, String searchPhone,
			EUserRole role, EUserStatus status, Page page);

	Long searchAdminCount(String searchFullname, String searchUsername, String searchEmail, String searchPhone,
			EUserRole role, EUserStatus status);

}