package com.lucistore.lucistorebe.entity.user.buyer;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.product.ProductVariation;

@Entity
@Table(name = "buyer_cart_detail")
public class BuyerCartDetail {
	@EmbeddedId
	private BuyerCartDetailPK id;

	@MapsId("idBuyer")
	@ManyToOne
	@JoinColumn(name = "id_buyer", insertable = false, updatable = false)
	private Buyer buyer;

	@MapsId("idProductVariation")
	@ManyToOne
	@JoinColumn(name = "id_product_variation", insertable = false, updatable = false)
	private ProductVariation productVariation;

	@NotNull
	private Long quantity = 0L;
}
