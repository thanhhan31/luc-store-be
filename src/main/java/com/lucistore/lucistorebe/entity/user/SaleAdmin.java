package com.lucistore.lucistorebe.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.lucistore.lucistorebe.utility.ERole;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "sale_admin")
public class SaleAdmin implements UserInfo {
	@Id
	@Column(name="id", updatable=false, unique=true)
	private Long id;
	
	@OneToOne(optional = false)
	@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
	private User user;
	
	@Column(name = "access_level_control")
	private String accessLevelControl;

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public ERole getRole() {
		return user.getRole();
	}

	@Override
	public boolean isActive() {
		return user.getStatus() == EUserStatus.ACTIVE;
	}
}
