package com.lucistore.lucistorebe.entity.user.buyer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.address.AddressWard;
import com.lucistore.lucistorebe.utility.EBuyerDeliveryAddressStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name = "buyer_delivery_address")
public class BuyerDeliveryAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_buyer")
	private Buyer buyer;
	
	@ManyToOne
	@JoinColumn(name = "id_address_ward")
	private AddressWard addressWard;
	
	@Column(name = "adress_detail")
	private String addressDetail;
	
	@Column(name = "receiver_name")
	private String receiverName;
	
	@Column(name = "receiver_phone")
	private String receiverPhone;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EBuyerDeliveryAddressStatus status;

	public BuyerDeliveryAddress( Buyer buyer, AddressWard addressWard, String addressDetail, String receiverName,
			String receiverPhone) {
		this.buyer = buyer;
		this.addressWard = addressWard;
		this.addressDetail = addressDetail;
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.status = EBuyerDeliveryAddressStatus.ACTIVE;
	}
}
