package com.lucistore.lucistorebe.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderDetailPK implements Serializable {
	@Column(name="id_order")
	private Long idOrder;
	
	@Column(name="id_product_variation")
	private Long idProductVariation;
}
