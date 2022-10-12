package com.lucistore.lucistorebe.entity.user.buyer;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.pk.BuyerFavouriteProductPK;
import com.lucistore.lucistorebe.entity.product.Product;

@Entity
@Table(name = "buyer_favourite_product")
public class BuyerFavouriteProduct {
	@EmbeddedId
	BuyerFavouriteProductPK id;

	@MapsId("idBuyer")
	@ManyToOne
	@JoinColumn(name = "id_buyer", insertable = false, updatable = false)
	private Buyer buyer;

	@MapsId("idProduct")
	@ManyToOne
	@JoinColumn(name = "id_product", insertable = false, updatable = false)
	private Product product;

	public BuyerFavouriteProduct( Buyer buyer, Product product) {
		this.buyer = buyer;
		this.product = product;
		this.id = new BuyerFavouriteProductPK(buyer.getId(), product.getId());
	}
}
