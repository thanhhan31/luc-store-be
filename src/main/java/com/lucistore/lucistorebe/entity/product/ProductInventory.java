package com.lucistore.lucistorebe.entity.product;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "product_inventory")
public class ProductInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_product_variation")
	private ProductVariation variation;
	
	@ManyToOne
	@JoinColumn(name = "id_importer")
	private User importer;
	
	@Column(name = "import_time")
	private Date importTime;
	
	@Column(name = "import_quantity")
	private Long importQuantity;
}
