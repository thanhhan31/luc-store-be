package com.lucistore.lucistorebe.entity.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable @AllArgsConstructor @NoArgsConstructor
public class BuyerFavouriteProductPK implements Serializable {
	private static final long serialVersionUID = 759301656114162854L;

	@Column(name="id_buyer")
	private Long idBuyer;

	@Column(name="id_product")
	private Long idProduct;
}
