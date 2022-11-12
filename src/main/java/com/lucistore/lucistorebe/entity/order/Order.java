package com.lucistore.lucistorebe.entity.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.EPaymentMethod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "order_")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_buyer")
	private Buyer buyer;
	
	@ManyToOne
	@JoinColumn(name = "id_seller")
	private User seller;

	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
	private List<OrderDetail> orderDetails = new ArrayList<>(); 
	
	@ManyToOne
	@JoinColumn(name = "id_buyer_delivery_address")
	private BuyerDeliveryAddress deliveryAddress;
	
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "price")
	private Long price;

	@Column(name = "payPrice")
	private Long payPrice;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EOrderStatus status;
	
	@Column(name = "payment_method")
	@Enumerated(EnumType.STRING)
	private EPaymentMethod paymentMethod;
	
	@Column(name = "reviewed") //if all order detail get reviewed then true
	private Boolean isAllOrderDetailReviewed;

	public Order(Buyer buyer, BuyerDeliveryAddress deliveryAddress, String note,
			EOrderStatus status, EPaymentMethod paymentMethod) {
		this.buyer = buyer;
		this.deliveryAddress = deliveryAddress;
		this.note = note;
		this.status = status;
		this.paymentMethod = paymentMethod;
		
		this.createTime = new Date();
		this.isAllOrderDetailReviewed = false;
	}

}
