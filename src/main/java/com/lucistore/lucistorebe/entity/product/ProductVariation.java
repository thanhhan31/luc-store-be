package com.lucistore.lucistorebe.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lucistore.lucistorebe.utility.EProductVariationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "product_variation")
public class ProductVariation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_product")
	private Product product;
	
	@Column(name = "variation_name")
	private String variationName;
	
	@Column(name = "price")
	private Long price;
	
	@Column(name = "available_quantity")
	private Long availableQuantity;
	
	@Column(name = "discount")
	private Integer discount;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EProductVariationStatus status;
	
	public ProductVariation(Product product, String variationName, Long price, Long availableQuantity,
			EProductVariationStatus status) {
		super();
		this.product = product;
		this.variationName = variationName;
		this.price = price;
		this.availableQuantity = availableQuantity;
		this.status = status;
	}

	public ProductVariation(Product product, String variationName, Long price, Long availableQuantity, Integer discount,
			EProductVariationStatus status) {
		this(product, variationName, price, availableQuantity, status);
		
		if (discount == null) {
			this.discount = 0;
		}
		else {
			this.discount = discount;
		}
	}
}
