package com.lucistore.lucistorebe.entity.product;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lucistore.lucistorebe.utility.EProductCategoryStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "product_category")
public class ProductCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_parent")
	private ProductCategory parent;
	
	@OneToMany(mappedBy = "parent")
	private List<ProductCategory> child;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "level", nullable = false)
	private Integer level;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EProductCategoryStatus status;

	public ProductCategory(String name) {
		super();
		this.name = name;
	}
	
	public ProductCategory(ProductCategory parent, String name) {
		super();
		this.parent = parent;
		this.name = name;
	}
}
