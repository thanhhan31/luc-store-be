package com.lucistore.lucistorebe.entity.user.buyer;

import java.util.List;

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
import com.lucistore.lucistorebe.utility.EGender;
import com.lucistore.lucistorebe.utility.ERole;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "buyer")
public class Buyer implements UserInfo {
	@Id
	@Column(name="id", updatable=false, unique=true)
	private Long id;
	
	@OneToOne(optional = false)
	@PrimaryKeyJoinColumn(name = "id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "id_avatar")
	private MediaResource avatar;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerCartDetail> cart;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerFavouriteProduct> favouriteProducts;

	@Column(name = "gender")
	@Enumerated(EnumType.ORDINAL)
	private EGender gender;

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
