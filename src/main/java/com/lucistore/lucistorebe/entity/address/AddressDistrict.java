package com.lucistore.lucistorebe.entity.address;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.entity.product.ProductVariation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "address_district")
public class AddressDistrict {
	@Id
	private Long id;
	
	@OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
	private List<AddressWard> communeWardTowns;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AddressProvinceCity provinceCity;
	
	@Column(name = "name")
	private String name;
}
