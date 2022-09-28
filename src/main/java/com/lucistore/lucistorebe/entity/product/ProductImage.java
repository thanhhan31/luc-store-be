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
@Table(name = "product_image")
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_product")
	private Product product;
	
	@OneToOne
	@JoinColumn(name = "id_media")
	private MediaResource media;

	public ProductImage(Product product, MediaResource media) {
		this.product = product;
		this.media = media;
	}
}
