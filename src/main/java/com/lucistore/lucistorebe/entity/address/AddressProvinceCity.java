package com.lucistore.lucistorebe.entity.address;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "address_province_city")
public class AddressProvinceCity {
	@Id
	private Long id;
	
	@OneToMany(mappedBy = "provinceCity", fetch = FetchType.LAZY)
	private List<AddressDistrict> districts;
	
	@Column(name = "name")
	private String name;
}
