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
import com.lucistore.lucistorebe.controller.payload.dto.UserDTO;
import com.lucistore.lucistorebe.controller.payload.response.LoginResponse;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.utility.EUserRole;
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
	
	public LoginResponse<?> authenticateWithUsernamePassword(String username, String password, EUserRole requestedRole) {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		@SuppressWarnings("rawtypes")
		UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
		String token = generateToken(username);
		
		EUserRole r = EUserRole.valueOf(userDetails.getUser().getRole().getName());
		
		if (requestedRole.equals(EUserRole.BUYER) && requestedRole.equals(r)) { // buyer
			return new LoginResponse<>(
					token, 
					mapper.map((Buyer)userDetails.getUser(), BuyerDTO.class)
				);
		}
		else if (requestedRole.equals(EUserRole.ADMIN) && !r.equals(EUserRole.BUYER)) {
			return new LoginResponse<>(
					token, 
					mapper.map((User)userDetails.getUser(), UserDTO.class)
				);
		}
		else {
			throw new LoginException("Username or password is invalid");
		}
	}
	
	public String generateToken(String username) {
		return jwtUtil.generateJwtToken(username);
	}
}
