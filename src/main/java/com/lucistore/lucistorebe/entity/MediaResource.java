package com.lucistore.lucistorebe.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "media_resource")
public class MediaResource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@NotBlank
	private String url;
	
	@Column(name = "public_id")
	private String publicId;
	
	@Column(name = "resource_type")
	private String resourceType;

	public MediaResource(String url, String publicId, String resourceType) {
		this.url = url;
		this.publicId = publicId;
		this.resourceType = resourceType;
	}
}
