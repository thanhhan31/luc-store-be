package com.lucistore.lucistorebe.entity.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;

@Embeddable @AllArgsConstructor
public class OrderDetailPK implements Serializable {
	private static final long serialVersionUID = 7703736379513092165L;

	@Column(name="id_order")
	private Long idOrder;
	
	@Column(name="id_product_variation")
	private Long idProductVariation;
}
