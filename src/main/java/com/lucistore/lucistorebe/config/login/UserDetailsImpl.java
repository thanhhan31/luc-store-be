package com.lucistore.lucistorebe.config.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lucistore.lucistorebe.entity.user.UserInfo;
import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = -5162310979327931940L;
	
	private UserInfo userInfo;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(UserInfo userInfo) {
		super();
		this.userInfo = userInfo;
		this.authorities = buildAuthority(userInfo);
	}
	
	public static UserDetailsImpl build(UserInfo userInfo) {
		return new UserDetailsImpl(userInfo);
	}
	
	public Collection<? extends GrantedAuthority> buildAuthority(UserInfo userInfo) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(
				"ROLE_" + userInfo.getRole().toString()
				)
			);
		return authorities;
	}

	@Override
	public String getPassword() {
		return userInfo.getPassword();
	}

	@Override
	public String getUsername() {
		return userInfo.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return userInfo.isActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return userInfo.isActive();
	}
}
