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

@Entity
@Table(name = "product_variation")
public class ProductVariation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_product")
	private Product product;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private Long price;
	
	@Column(name = "available_quantity")
	private Long availableQuantity;
	
	@Column(name = "discount")
	private Integer discount;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EProductVariationStatus status;
}
