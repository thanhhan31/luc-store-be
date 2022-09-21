package com.lucistore.lucistorebe.entity.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress;
import com.lucistore.lucistorebe.utility.EOrderStatus;

@Entity
@Table(name = "order")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_buyer")
	private Buyer buyer;
	
	@ManyToOne
	@JoinColumn(name = "id_buyer_delivery_address")
	private BuyerDeliveryAddress deliveryAddress;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EOrderStatus status;
}
