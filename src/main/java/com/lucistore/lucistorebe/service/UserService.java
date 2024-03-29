package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.UserDTO;
import com.lucistore.lucistorebe.controller.payload.request.AdminUpdateUserStatusRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.OrderRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.repo.UserRoleRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.UserFilter;

@Service
public class UserService {
	@Autowired
	LogService logService;
	
	@Autowired
	BuyerRepo buyerRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	UserRoleRepo userRoleRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<UserDTO> getById(Long id) {
		return serviceUtils.convertToDataResponse(
				userRepo.getReferenceById(id),
				UserDTO.class
			);
	}
	
	public ListWithPagingResponse<UserDTO> search(UserFilter filter, PagingInfo pagingInfo) {
		return serviceUtils.convertToListResponse(
				userRepo.search(filter, pagingInfo),
				UserDTO.class
			);
	}
	
	public DataResponse<UserDTO> updateStatus(Long idUser, Long id, AdminUpdateUserStatusRequest data) {
		var ret = updateStatus(id, data);
		logService.logInfo(idUser, 
				String.format("Change user account status with id %d to %s", id, data.getStatus().equals(EUserStatus.BANNED) ? "BANNED" : "ACTIVE"));
		return ret;
	}
	
	public DataResponse<UserDTO> updateStatus(Long id, AdminUpdateUserStatusRequest data) {
		if (!userRepo.existsById(id))
			throw new InvalidInputDataException("No user found with given id");
		
		User user = userRepo.getReferenceById(id);
		
		if (data.getStatus().equals(EUserStatus.WAIT_BANNED))
			throw new InvalidInputDataException("Status 'WAIT_BANNED' is not allowed");
		
		if (!data.getStatus().equals(user.getStatus())) {
			if (data.getStatus() == EUserStatus.BANNED) {
				if (user.getRole().getName().equals(EUserRole.BUYER.toString()) && orderRepo.isBuyerHavePendingOrder(id)) { //if is buyer and have active order (not complete)
					user.setStatus(EUserStatus.WAIT_BANNED);
				}
				else 
					user.setStatus(data.getStatus());
			}
			else 
				user.setStatus(data.getStatus());
		}
		
		return serviceUtils.convertToDataResponse(
				userRepo.save(user),
				UserDTO.class
			);
	}
}
