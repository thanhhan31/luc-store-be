package com.lucistore.lucistorebe.entity.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BuyerCartDetailPK {
	@Column(name="id_buyer")
	private Long idBuyer;
	
	@Column(name="id_product_variation")
	private Long idProductVariation;
}
