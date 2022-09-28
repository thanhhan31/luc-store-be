package com.lucistore.lucistorebe.entity.product;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_category")
	private ProductCategory category;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<ProductVariation> variations;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "min_price")
	private Long minPrice;
	
	@Column(name = "max_price")
	private Long maxPrice;
	
	@Column(name = "nsold")
	private Long nsold;
	
	@Column(name = "nvisit")
	private Long nvisit;
}
