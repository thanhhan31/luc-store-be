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
import javax.persistence.UniqueConstraint;

import com.lucistore.lucistorebe.utility.EProductCategoryStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "product_category", 
	uniqueConstraints = {
			@UniqueConstraint(columnNames = {"name", "level"})
	}
)
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
	
	@Column(name = "level")
	private Integer level;
	
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private EProductCategoryStatus status;

	public ProductCategory(String name) {
		this.name = name;
		this.status = EProductCategoryStatus.ACTIVE;
		level = 0;
	}
	
	public ProductCategory(ProductCategory parent, String name) {
		this.parent = parent;
		this.name = name;
		this.status = EProductCategoryStatus.ACTIVE;
		level = parent.level + 1;
	}
}
