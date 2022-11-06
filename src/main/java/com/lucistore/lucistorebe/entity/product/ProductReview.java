package com.lucistore.lucistorebe.entity.product;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.user.buyer.Buyer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "product_review")
public class ProductReview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_buyer")
	private Buyer buyer;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_product_variation")
	private ProductVariation productVariation;
	
	@OneToMany(mappedBy = "productReview")
	private List<ProductReviewImage> images;
	
	@Column(name = "point")
	private Integer point;
	
	@Lob
	@Column(name = "content")
	private String content;
	
	@Column(name = "time")
	private Date time;

	public ProductReview(Buyer buyer, ProductVariation productVariation, Integer point, String content) {
		this.buyer = buyer;
		this.productVariation = productVariation;
		this.point = point;
		this.content = content;
		
		this.time = new Date();
	}
}
