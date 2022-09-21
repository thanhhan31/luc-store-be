package com.lucistore.lucistorebe.entity.user.buyer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.address.AddressWard;
import com.lucistore.lucistorebe.utility.EBuyerDeliveryAddressStatus;

@Entity
@Table(name = "buyer_delivery_address")
public class BuyerDeliveryAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_buyer")
	private Long idBuyer;
	
	@Column(name = "id_address_ward")
	private AddressWard addressWard;
	
	@Column(name = "adress_detail")
	private String addressDetail;
	
	@Column(name = "receiver_name")
	private String receiverName;
	
	@Column(name = "receiver_phone")
	private String receiverPhone;
	
	@Column(name = "default_address")
	private Boolean isDefault;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EBuyerDeliveryAddressStatus status;
}
