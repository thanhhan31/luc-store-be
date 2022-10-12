package com.lucistore.lucistorebe.entity.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
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
