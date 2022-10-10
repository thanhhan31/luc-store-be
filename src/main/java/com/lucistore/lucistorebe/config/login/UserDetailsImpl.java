package com.lucistore.lucistorebe.config.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lucistore.lucistorebe.entity.user.UserInfo;
import com.lucistore.lucistorebe.entity.user.UserRole;
import com.lucistore.lucistorebe.utility.ERolePermission;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;

@Getter
public class UserDetailsImpl<T extends UserInfo> implements UserDetails {
	private static final long serialVersionUID = -5162310979327931940L;
	
	private T user;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(T userInfo) {
		super();
		this.user = userInfo;
		this.authorities = buildAuthority(userInfo);
	}
	
	public Collection<? extends GrantedAuthority> buildAuthority(T userInfo) {
		UserRole role = userInfo.getRole();
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		authorities.add(new SimpleGrantedAuthority(
				"ROLE_" + role.getName()
			)
		);
		
		if (userInfo.getStatus().equals(EUserStatus.WAIT_BANNED)) {
			authorities.add(new SimpleGrantedAuthority(ERolePermission.BANNED_BUYER.toString()));
		}
		
		for (ERolePermission permission : role.getPermissions()) {
			authorities.add(new SimpleGrantedAuthority(permission.toString()));
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.isActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isActive();
	}
}
