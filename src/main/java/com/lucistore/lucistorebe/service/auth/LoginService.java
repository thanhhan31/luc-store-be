package com.lucistore.lucistorebe.service.auth;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.config.login.UserDetailsImpl;
import com.lucistore.lucistorebe.controller.advice.exception.LoginException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerDTO;
import com.lucistore.lucistorebe.controller.payload.dto.SaleAdminDTO;
import com.lucistore.lucistorebe.controller.payload.dto.UserDTO;
import com.lucistore.lucistorebe.controller.payload.response.LoginResponse;
import com.lucistore.lucistorebe.entity.user.SaleAdmin;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.SaleAdminRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.utility.ERole;
import com.lucistore.lucistorebe.utility.jwt.JwtUtil;

@Service
public class LoginService {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BuyerRepo buyerRepo;
	
	@Autowired
	SaleAdminRepo saleAdminRepo;
	
	public LoginResponse<?> authenticateWithUsernamePassword(String username, String password, ERole requestedRole) {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
		String token = jwtUtil.generateJwtToken(username);
		
		ERole r = userDetails.getUserInfo().getRole();
		
		if (!r.equals(requestedRole))
			throw new LoginException("Username or password is invalid");
		
		if (r == ERole.ADMIN) {
			return new LoginResponse<>(
					token, 
					mapper.map((User)userDetails.getUserInfo(), UserDTO.class)
				);
		}
		else if (r == ERole.SALE_ADMIN) {
			return new LoginResponse<>(
					token, 
					mapper.map((SaleAdmin)userDetails.getUserInfo(), SaleAdminDTO.class)
				);
		}
		else if (r == ERole.BUYER) {
			return new LoginResponse<>(
					token, 
					mapper.map((Buyer)userDetails.getUserInfo(), BuyerDTO.class)
				);
		}
		else
			throw new IllegalArgumentException("Unexpected role value: " + r);
	}
}
