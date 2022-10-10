package com.lucistore.lucistorebe.entity.user.buyer;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.entity.UpdatableAvatar;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "buyer")
public class Buyer implements UserInfo, UpdatableAvatar {
	@Id
	@Column(name="id", updatable=false, unique=true)
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL,optional = false)
	@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "id_avatar")
	private MediaResource avatar;
	
	@ManyToOne
	@JoinColumn(name = "id_rank"/*, nullable = false*/)
	private BuyerRank rank;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerCartDetail> cart;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerFavouriteProduct> favouriteProducts;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerDeliveryAddress> deliveryAddresses;
	
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

	@Column(name = "total_spent")
	private Long totalSpent;
	
	@Column(name = "created_date")
	@CreatedDate
	private Date createdDate;

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
		return user.getStatus().equals(EUserStatus.ACTIVE) || user.getStatus().equals(EUserStatus.WAIT_BANNED);
	}
	
	@Override
	public EUserStatus getStatus() {
		return user.getStatus();
	}
	
	public Buyer(User user, Boolean canChangeUsername, Boolean emailConfirmed, Boolean phoneConfirmed) {
		this.id = user.getId();
		this.user = user;
		this.canChangeUsername = canChangeUsername;
		this.emailConfirmed = emailConfirmed;
		this.phoneConfirmed = phoneConfirmed;
	}
	
	public Buyer(User user, EGender gender,
			Boolean canChangeUsername, Boolean emailConfirmed, Boolean phoneConfirmed) {
		this(user, canChangeUsername, emailConfirmed, phoneConfirmed);
		this.gender = gender;
	}
}
