package com.lucistore.lucistorebe.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lucistore.lucistorebe.utility.ERole;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "user")
public class User implements UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "user_name", unique = true)
	private String username;
	
	@Column(name = "phone", unique = true)
	private String phone;
	
	@Column(name = "role")
	@Enumerated(EnumType.ORDINAL)
	private ERole role;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EUserStatus status;

	@Override
	public boolean isActive() {
		return status == EUserStatus.ACTIVE;
	}
}
