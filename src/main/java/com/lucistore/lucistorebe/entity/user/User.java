package com.lucistore.lucistorebe.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedBy;

import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
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
	
	@Column(name = "full_name")
	private String fullname;
	
	@Column(name = "user_name", unique = true)
	private String username;
	
	@Column(name = "phone", unique = true)
	private String phone;
	
	@ManyToOne(optional = false)
	private UserRole role;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EUserStatus status;
	
	@Column(name = "last_modified_by")
	@LastModifiedBy
	private String lastModifiedBy;
	
	/*@Column(name = "last_modified_date")
	@LastModifiedDate
	private Date lastModifiedDate;*/

	@Override
	public boolean isActive() {
		return status == EUserStatus.ACTIVE;
	}
	
	public User(String email, String username, String fullname, UserRole role, EUserStatus status) {
		this.email = email;
		this.username = username;
		this.fullname = fullname;
		this.role = role;
		this.status = status;
	}
	
	public User(String email, String password, String username, String fullname, UserRole role, EUserStatus status) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.fullname = fullname;
		this.role = role;
		this.status = status;
	}
}
