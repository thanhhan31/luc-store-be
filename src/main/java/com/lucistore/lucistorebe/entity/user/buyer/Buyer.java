package com.lucistore.lucistorebe.entity.user.buyer;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.UserInfo;
import com.lucistore.lucistorebe.entity.user.UserRole;
import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "buyer")
public class Buyer implements UserInfo {
	@Id
	@Column(name="id", updatable=false, unique=true)
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL,optional = false)
	@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "id_avatar")
	private MediaResource avatar;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerCartDetail> cart;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerFavouriteProduct> favouriteProducts;
	
	@Column(name = "dob")
	private Date dob;

	@Column(name = "gender")
	@Enumerated(EnumType.ORDINAL)
	private EGender gender;
	
	@Column(name = "can_change_username")
	private Boolean canChangeUsername;
	
	@Column(name = "email_confirmed")
	private Boolean emailConfirmed;
	
	@Column(name = "phone_confirmed")
	private Boolean phoneConfirmed;

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public UserRole getRole() {
		return user.getRole();
	}

	@Override
	public boolean isActive() {
		return user.getStatus() == EUserStatus.ACTIVE;
	}
	
	/*public Buyer(String fullname, String username, String password, String email, EGender gender,
			Boolean canChangeUsername, Boolean emailConfirmed, Boolean phoneConfirmed) {
		this.user = new User(email, password, username, fullname, ERole.BUYER, EUserStatus.ACTIVE);
		this.gender = gender;
		this.canChangeUsername = canChangeUsername;
		this.emailConfirmed = emailConfirmed;
		this.phoneConfirmed = phoneConfirmed;
	}*/
	
	public Buyer(User user, EGender gender,
			Boolean canChangeUsername, Boolean emailConfirmed, Boolean phoneConfirmed) {
		this.id = user.getId();
		this.user = user;
		this.gender = gender;
		this.canChangeUsername = canChangeUsername;
		this.emailConfirmed = emailConfirmed;
		this.phoneConfirmed = phoneConfirmed;
	}
}
