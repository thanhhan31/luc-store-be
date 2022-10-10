package com.lucistore.lucistorebe.config.login;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.utility.EUserRole;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BuyerRepo buyerRepo;
	
	private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");//^(.+)@(\\S+)$
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String loginKey) throws UsernameNotFoundException {
		User user = null;
		if (isPhoneNumber(loginKey)) {
			user = userRepo.findByPhone(loginKey);
		}
		else if (isEmail(loginKey)) {
			user = userRepo.findByEmail(loginKey);
		}
		else {
			user = userRepo.findByUsername(loginKey);
		}
		
		if (user != null) {
			Long uid = user.getId();
			
			switch (EUserRole.valueOf(user.getRole().getName())) {
			case ADMIN, SALE_ADMIN: {
				return new UserDetailsImpl<User>(user);
			}
			case BUYER: {
				Buyer buyer = buyerRepo.findById(user.getId()).orElseThrow(
						() -> new UsernameNotFoundException(String.format("User not found with id %d", uid))
					);
				return new UserDetailsImpl<Buyer>(buyer);
			}
			default:
				throw new IllegalArgumentException("Unexpected role value: " + user.getRole());
			}
		}
		else
			throw new UsernameNotFoundException("No user found with given login key");
	}
	
	private boolean isPhoneNumber(String key) {
		return PHONE_PATTERN.matcher(key).find();
	}
	
	private boolean isEmail(String key) {
		return EMAIL_PATTERN.matcher(key).find();
	}
}
