package com.lucistore.lucistorebe.entity.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address_commune_ward_town")
public class AddressWard {
	@Id
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AddressDistrict district;
	
	@Column(name = "name")
	private String name;
}
