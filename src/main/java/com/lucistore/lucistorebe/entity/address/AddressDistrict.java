package com.lucistore.lucistorebe.entity.address;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
