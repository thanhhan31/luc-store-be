package com.lucistore.lucistorebe.entity.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.MediaResource;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "product_review_image")
public class ProductReviewImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_product_review")
	private ProductReview productReview;
	
	@OneToOne
	@JoinColumn(name = "id_media")
	private MediaResource media;

	public ProductReviewImage(ProductReview productReview, MediaResource media) {
		this.productReview = productReview;
		this.media = media;
	}
}
