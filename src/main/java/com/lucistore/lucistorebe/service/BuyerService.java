package com.lucistore.lucistorebe.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRestException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerDTO;
import com.lucistore.lucistorebe.controller.payload.request.BuyerUpdateProfileRequest;
import com.lucistore.lucistorebe.controller.payload.request.SignupUsernamePasswordRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.repo.UserRoleRepo;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.ServiceDataReturnConverter;

@Service
public class BuyerService {
	@Autowired
	BuyerRepo buyerRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserRoleRepo userRoleRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ServiceDataReturnConverter dataReturnConverter;
	
	public DataResponse<BuyerDTO> getById(Long id) {
		return dataReturnConverter.convertToDataResponse(
			buyerRepo.findById(id).orElseThrow(
					() -> new CommonRestException("No user found with given id")
				),
			BuyerDTO.class);
	}
	
	public DataResponse<BuyerDTO> create(SignupUsernamePasswordRequest data) {
		validateSignupData(data);

		Buyer buyer = new Buyer(
				userRepo.save(
					new User(
						data.getEmail(), 
						passwordEncoder.encode(data.getPassword()), 
						data.getUsername(), 
						data.getFullname(), 
						userRoleRepo.getReferenceById(EUserRole.BUYER.toString()),
						EUserStatus.ACTIVE
					)
				),
				data.getGender(),
				false,
				false,
				false
			);
		
		return dataReturnConverter.convertToDataResponse(
			buyerRepo.save(buyer),
			BuyerDTO.class);
	}
	
	public DataResponse<BuyerDTO> update(Long id, BuyerUpdateProfileRequest data) {
		Buyer buyer = buyerRepo.getReferenceById(id);
		validateUpdateData(buyer, data);
		
		if (StringUtils.isNotEmpty(data.getUsername()) && !data.getUsername().equals(buyer.getUsername())) {
			if (buyer.getCanChangeUsername().booleanValue()) {
				buyer.getUser().setUsername(data.getUsername());
				buyer.setCanChangeUsername(false);
			}
			else
				throw new InvalidInputDataException("Can not change username");
		}
		
		if (StringUtils.isNotEmpty(data.getEmail()) && !data.getEmail().equals(buyer.getUser().getEmail()))
			buyer.getUser().setEmail(data.getEmail());
		
		if (StringUtils.isNotEmpty(data.getPhone()) && !data.getPhone().equals(buyer.getUser().getPhone()))
			buyer.getUser().setPhone(data.getPhone());
		
		if (StringUtils.isNotEmpty(data.getFullname()) && !data.getFullname().equals(buyer.getUser().getFullname()))
			buyer.getUser().setFullname(data.getFullname());
		
		if (data.getGender() != null && !data.getGender().equals(buyer.getGender())) {
			buyer.setGender(data.getGender());
		}
		
		if (data.getDob() != null && !data.getDob().equals(buyer.getDob())) {
			buyer.setDob(data.getDob());
		}
		
		return dataReturnConverter.convertToDataResponse(
			buyerRepo.save(buyer),
			BuyerDTO.class
		);
	}
	
	private void validateSignupData(SignupUsernamePasswordRequest data) {
		if (buyerRepo.existsByUser_Username(data.getUsername()).booleanValue()) {
			throw new InvalidInputDataException("Username is already exists");
		}
		if (buyerRepo.existsByUser_Email(data.getEmail()).booleanValue()) {
			throw new InvalidInputDataException("Email is already exists");
		}
	}
	
	private void validateUpdateData(Buyer buyer, BuyerUpdateProfileRequest data) {
		if (StringUtils.isNotEmpty(data.getUsername()) && 
				!buyer.getUser().getUsername().equals(data.getUsername()) && 
				buyerRepo.existsByUser_Username(data.getUsername()).booleanValue())
			throw new InvalidInputDataException("Username is already exists");
		
		if (StringUtils.isNotEmpty(data.getEmail()) &&
				!buyer.getUser().getEmail().equals(data.getEmail()) && 
				buyerRepo.existsByUser_Email(data.getEmail()).booleanValue())
			throw new InvalidInputDataException("Email is already exists");
		
		if (StringUtils.isNotEmpty(data.getPhone()) && 
				!buyer.getUser().getPhone().equals(data.getPhone()) && 
				buyerRepo.existsByUser_Phone(data.getPhone()).booleanValue())
			throw new InvalidInputDataException("Phone is already used");
	}
	
}
