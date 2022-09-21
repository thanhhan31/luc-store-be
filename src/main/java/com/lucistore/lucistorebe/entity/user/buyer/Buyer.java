package com.lucistore.lucistorebe.entity.user.buyer;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.user.User;

@Entity
@Table(name = "buyer")
public class Buyer {
	@Id
	@Column(name="id", updatable=false, unique=true)
	private Long id;
	
	@OneToOne(optional = false)
	@PrimaryKeyJoinColumn(name = "id")
	private User user;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerCartDetail> cart;
	
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<BuyerFavouriteProduct> favouriteProducts;
	
	@Column(name = "avatar")
	private String avatar;
	
	@Column(name = "gender")
	private String gender;
}
