package com.lucistore.lucistorebe.entity.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BuyerFavouriteProductPK {
	@Column(name="id_buyer")
	private Long idBuyer;

	@Column(name="id_product")
	private Long idProduct;
}
