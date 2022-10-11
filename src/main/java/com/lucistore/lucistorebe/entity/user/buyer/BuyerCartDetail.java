package com.lucistore.lucistorebe.entity.user.buyer;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.product.ProductVariation;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.EProductVariationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @NoArgsConstructor
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

	public BuyerCartDetail(Buyer buyer, ProductVariation productVariation, Long quantity) {
		this.buyer = buyer;
		this.productVariation = productVariation;
		this.quantity = quantity;
		this.id = new BuyerCartDetailPK(buyer.getId(), productVariation.getId());
	}

	@PrePersist
	@PreUpdate
	private void check(){
		if (productVariation.getStatus() != EProductVariationStatus.ENABLED) {
			throw new InvalidInputDataException("Product Variation is not enabled");
		}

		if (productVariation.getProduct().getStatus() != EProductStatus.ENABLED) {
			throw new InvalidInputDataException("Product is not enabled");
		}

		if (quantity > productVariation.getAvailableQuantity()) {
			throw new InvalidInputDataException("Quantity cannot be greater than stock");
		}
	}
}
