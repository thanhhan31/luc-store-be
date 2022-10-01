package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.UserDTO;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.repo.UserRoleRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.Page;

@Service
public class UserService {
	@Autowired
	BuyerRepo buyerRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserRoleRepo userRoleRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public ListWithPagingResponse<UserDTO> searchAdmin(String searchFullname, String searchUsername, String searchEmail,
			String searchPhone, EUserRole role, EUserStatus status, Integer currentPage, Integer size, Sort sort) {
		
		int count = userRepo.searchAdminCount(searchFullname, searchUsername, searchEmail, searchPhone, role, status).intValue();
		Page page = new Page(currentPage, size, count, sort);
		
		return serviceUtils.convertToListResponse(
				userRepo.searchAdmin(searchFullname, searchUsername, searchEmail, searchPhone, role, status, page),
				UserDTO.class, 
				page
			);
	}
}
