package com.lucistore.lucistorebe.entity.order;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.product.ProductVariation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {
	@EmbeddedId
	private OrderDetailPK id;
	
	@ManyToOne
	@MapsId("idOrder")
	@JoinColumn(name = "id_order", insertable = false, updatable = false)
	private Order order;

	@ManyToOne
	@MapsId("idProductVariation")
	@JoinColumn(name = "id_product_variation", insertable = false, updatable = false)
	private ProductVariation productVariation;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "unit_price")
	private Long unitPrice;

	public OrderDetail(Order order, ProductVariation productVariation, Long quantity, Long unitPrice) {
		this.order = order;
		this.productVariation = productVariation;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.id = new OrderDetailPK(order.getId(), productVariation.getId());
	}
}
