package com.lucistore.lucistorebe.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRestException;
import com.lucistore.lucistorebe.controller.advice.exception.DataConflictException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerDTO;
import com.lucistore.lucistorebe.controller.payload.request.AdminUpdateUserStatusRequest;
import com.lucistore.lucistorebe.controller.payload.request.BuyerUpdateProfileRequest;
import com.lucistore.lucistorebe.controller.payload.request.PasswordResetRequest;
import com.lucistore.lucistorebe.controller.payload.request.PasswordUpdateRequest;
import com.lucistore.lucistorebe.controller.payload.request.SignupUsernamePasswordRequest;
import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.repo.UserRoleRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.EUserRole;
import com.lucistore.lucistorebe.utility.EUserStatus;
import com.lucistore.lucistorebe.utility.OtpCache;
import com.lucistore.lucistorebe.utility.Page;
import com.lucistore.lucistorebe.utility.RandomString;

@Service
public class BuyerService {
	@Autowired
	LogService logService;
	
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
	
	@Autowired
	OtpCache otpCache;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MediaResourceService mediaResourceService;
	
	public DataResponse<BuyerDTO> updatePassword(Long id, PasswordUpdateRequest data) {
		Buyer buyer = buyerRepo.getReferenceById(id);
		
		if (data.getOtp().equals(otpCache.get(buyer.getUsername()))) {
			if (StringUtils.isBlank(buyer.getUser().getPassword()) || passwordEncoder.matches(data.getOldPassword(), buyer.getPassword())) {
				buyer.getUser().setPassword(passwordEncoder.encode(data.getNewPassword()));
				return serviceUtils.convertToDataResponse(buyerRepo.save(buyer), BuyerDTO.class);
			} 
			else 
				throw new InvalidInputDataException("Wrong old password");
		}
		else 
			throw new InvalidInputDataException("Invalid OTP code");
		
	}
	
	public ListWithPagingResponse<BuyerDTO> searchBuyer(String searchFullname, String searchUsername, String searchEmail, 
			String searchPhone, EUserStatus status, Date dob, EGender gender, Boolean emailConfirmed, Boolean phoneConfirmed,
			Date createdDate, String lastModifiedBy, Integer currentPage, Integer size, Integer sortBy, Boolean sortDescending) {
		
		int count = buyerRepo.searchBuyerCount(searchFullname, searchUsername, searchEmail, searchPhone, status, dob, gender, emailConfirmed, phoneConfirmed, createdDate, lastModifiedBy).intValue();
		Page page = new Page(currentPage, size, count, sortBy, sortDescending);
		
		return serviceUtils.convertToListResponse(
				buyerRepo.searchBuyer(searchFullname, searchUsername, searchEmail, searchPhone, status, dob, gender, emailConfirmed, phoneConfirmed, createdDate, lastModifiedBy, page),
				BuyerDTO.class, 
				page
			);
	}
	
	public DataResponse<BuyerDTO> getById(Long id) {
		return serviceUtils.convertToDataResponse(
			buyerRepo.findById(id).orElseThrow(
					() -> new CommonRestException("No user found with given id")
				),
			BuyerDTO.class);
	}
	
	public String getUsernameByEmail(String email) {
		return buyerRepo.findByUser_Email(email).getUsername();
	}
	
	public Buyer getBuyerByEmail(String email) {
		return buyerRepo.findByUser_Email(email);
	}
	
	public Buyer getBuyerByPhoneNumber(String phoneNumber) {
		return buyerRepo.findByUser_Phone(phoneNumber);
	}
	
	public boolean existsByEmail(String email) {
		return buyerRepo.existsByUser_Email(email);
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
		// send otp code to email ?
		
		return serviceUtils.convertToDataResponse(
			buyerRepo.save(buyer),
			BuyerDTO.class);
	}
	
	public String createOAuth2User(String email, String fullname) {
		String username = generateUsername(email.substring(0, email.indexOf("@")));
		
		Buyer buyer = new Buyer(
				userRepo.save(
					new User(
						email,
						username, 
						fullname, 
						userRoleRepo.getReferenceById(EUserRole.BUYER.toString()),
						EUserStatus.ACTIVE
					)
				),
				true,
				true,
				false
			);
		buyerRepo.save(buyer);
		return username;
	}
	
	public DataResponse<BuyerDTO> updateStatus(Long idUser, Long id, AdminUpdateUserStatusRequest data) {
		userService.updateStatus(id, data);
		
		logService.logInfo(idUser, 
				String.format("Change buyer account status with id %d to %s", id, data.getStatus().equals(EUserStatus.BANNED) ? "BANNED" : "ACTIVE"));
		
		return serviceUtils.convertToDataResponse(
				buyerRepo.getReferenceById(id),
				BuyerDTO.class
			);		
	}
	
	public DataResponse<BuyerDTO> update(Long id, BuyerUpdateProfileRequest data, MultipartFile avatar) {
		Buyer buyer = buyerRepo.getReferenceById(id);
		validateUpdateData(buyer, data);
		
		if (data != null) {
			if (StringUtils.isNotEmpty(data.getUsername()) && !data.getUsername().equals(buyer.getUsername())) {
				if (buyer.getCanChangeUsername().booleanValue()) {
					buyer.getUser().setUsername(data.getUsername());
					buyer.setCanChangeUsername(false);
				}
				else
					throw new InvalidInputDataException("Can not change username");
			}
			
			if (StringUtils.isNotEmpty(data.getEmail()) && !data.getEmail().equals(buyer.getUser().getEmail())) {
				buyer.getUser().setEmail(data.getEmail());
				buyer.setEmailConfirmed(false);
			}
			
			if (StringUtils.isNotEmpty(data.getPhone()) && !data.getPhone().equals(buyer.getUser().getPhone())) {
				buyer.getUser().setPhone(data.getPhone());
				buyer.setPhoneConfirmed(false);
			}
			
			if (StringUtils.isNotEmpty(data.getFullname()) && !data.getFullname().equals(buyer.getUser().getFullname()))
				buyer.getUser().setFullname(data.getFullname());
			
			if (data.getGender() != null && !data.getGender().equals(buyer.getGender())) {
				buyer.setGender(data.getGender());
			}
			
			if (data.getDob() != null && !data.getDob().equals(buyer.getDob())) {
				buyer.setDob(data.getDob());
			}
		}
		
		if (avatar != null) {
			serviceUtils.updateAvatar(buyer, avatar);
		}
		
		return serviceUtils.convertToDataResponse(
			buyerRepo.save(buyer),
			BuyerDTO.class
		);
	}
	
	public BaseResponse resetPassword(PasswordResetRequest data) {
		Buyer buyer = null;
		if (StringUtils.isNotBlank(data.getEmail())) 
			buyer = buyerRepo.findByUser_Email(data.getEmail());
		else if (StringUtils.isNotBlank(data.getPhone())) 
			buyer = buyerRepo.findByUser_Phone(data.getPhone());
		else
			throw new InvalidInputDataException("Email or phone number is required");
		
		if (otpCache.get(buyer.getUsername()).equals(data.getOtp())) {
			buyer.getUser().setPassword(passwordEncoder.encode(data.getNewPassword()));
			buyerRepo.save(buyer);
			return new BaseResponse();
		}
		else
			throw new InvalidInputDataException("OTP code is invalid");
	}
	
	private String generateUsername(String username) {
		if (buyerRepo.existsByUser_Username(username).booleanValue()) {
			String usernameOrgin = username;
			do {
				username = RandomString.generateUsername(usernameOrgin);
			} while (buyerRepo.existsByUser_Username(username).booleanValue());
		}
		
		return username;
	}
	
	private void validateSignupData(SignupUsernamePasswordRequest data) {
		if (buyerRepo.existsByUser_Username(data.getUsername()).booleanValue()) {
			throw new DataConflictException("Username is already exists");
		}
		if (buyerRepo.existsByUser_Email(data.getEmail()).booleanValue()) {
			throw new DataConflictException("Email is already exists");
		}
	}
	
	private void validateUpdateData(Buyer buyer, BuyerUpdateProfileRequest data) {
		if (data == null)
			return;
		
		if (StringUtils.isNotEmpty(data.getUsername()) && 
				!buyer.getUser().getUsername().equals(data.getUsername()) && 
				buyerRepo.existsByUser_Username(data.getUsername()).booleanValue())
			throw new DataConflictException("Username is already exists");
		
		if (StringUtils.isNotEmpty(data.getEmail()) &&
				!buyer.getUser().getEmail().equals(data.getEmail()) && 
				buyerRepo.existsByUser_Email(data.getEmail()).booleanValue())
			throw new DataConflictException("Email is already exists");
		
		if (StringUtils.isNotEmpty(data.getPhone()) && 
				!data.getPhone().equals(buyer.getUser().getPhone()) && 
				buyerRepo.existsByUser_Phone(data.getPhone()).booleanValue())
			throw new DataConflictException("Phone is already used");
		
		if (StringUtils.isNotEmpty(data.getEmail()) || StringUtils.isNotEmpty(data.getPhone())) {
			if (StringUtils.isEmpty(data.getOtp())) {
				throw new InvalidInputDataException("OTP code is required in order to change email/phone");
			}
			else if (!data.getOtp().equals(otpCache.get(buyer.getUsername()))) {
				throw new InvalidInputDataException("OTP code is invalid");
			}
		}
	}
}
