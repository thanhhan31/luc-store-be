package com.lucistore.lucistorebe.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "sale_admin")
public class SaleAdmin {
	@Id
	@Column(name="id", updatable=false, unique=true)
	private Long id;
	
	@OneToOne(optional = false)
	@PrimaryKeyJoinColumn(name = "id")
	private User user;
	
	@Column(name = "access_level_control")
	private String accessLevelControl;
}
