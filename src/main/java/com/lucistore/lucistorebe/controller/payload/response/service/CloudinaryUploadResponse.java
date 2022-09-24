package com.lucistore.lucistorebe.controller.payload.response.service;

import com.lucistore.lucistorebe.controller.advice.exception.RemoteUploadException;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CloudinaryUploadResponse {
	private String url;
	private String publicId;
	private String resourceType;
	
	public CloudinaryUploadResponse(String url, String publicId, String resourceType) {
		if (url.isEmpty()) {
			throw new RemoteUploadException("Upload file to cloudinary failed (no URL to access uploaded media found in Cloudinary response)");
		}
		if (publicId.isEmpty()) {
			throw new RemoteUploadException("Upload file to cloudinary failed (no public_id found in Cloudinary response)");
		}
		if (resourceType.isEmpty()) {
			throw new RemoteUploadException("Upload file to cloudinary failed (no resource_type found in Cloudinary response)");
		}
		
		this.url = url;
		this.publicId = publicId;
		this.resourceType = resourceType;
	}
}
