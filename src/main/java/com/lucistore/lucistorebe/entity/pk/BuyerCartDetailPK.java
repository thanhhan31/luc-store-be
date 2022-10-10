package com.lucistore.lucistorebe.entity.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;

@Embeddable @AllArgsConstructor
public class BuyerCartDetailPK implements Serializable {
	private static final long serialVersionUID = -4710107016894941014L;

	@Column(name="id_buyer")
	private Long idBuyer;
	
	@Column(name="id_product_variation")
	private Long idProductVariation;
	
}
