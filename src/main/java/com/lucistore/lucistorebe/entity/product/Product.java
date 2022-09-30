package com.lucistore.lucistorebe.entity.product;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.utility.EProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
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
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<ProductImage> images;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avatar")
	private MediaResource avatar;
	
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
	
	@Column(name = "status", nullable = false)
	private EProductStatus status;
	
	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;

	@Column(name = "created_date")
	@CreatedDate
	private Date createdDate;
	
	@Column(name = "last_modified_by")
	@LastModifiedBy
	private String lastModifiedBy;
	
	@Column(name = "last_modified_date")
	@LastModifiedDate
	private Date lastModifiedDate;

	public Product(ProductCategory category, String name, String description, MediaResource avatar) {
		super();
		this.category = category;
		this.name = name;
		this.description = description;
		this.avatar = avatar;
		
		this.minPrice = 0L;
		this.maxPrice = 0L;
		this.nsold = 0L;
		this.nvisit = 0L;
	}
}
