package com.lucistore.lucistorebe.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.response.service.CloudinaryUploadResponse;
import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.repo.MediaResourceRepo;

@Service
public class MediaResourceService {
	@Autowired
	private MediaResourceRepo repo;
	
	@Autowired
	CloudinaryService cloudinaryService;
	
	public MediaResource save(byte[] data) {
		try {
			CloudinaryUploadResponse resp =  cloudinaryService.upload(data);
			MediaResource m = new MediaResource(
					resp.getUrl(), 
					resp.getPublicId(),
					resp.getResourceType());
			return repo.save(m);
		} catch (IOException e) {
			throw new InvalidInputDataException("IOException occurred when upload data to Cloudinary service (" + e.getMessage() + ")");
		}
	}
	
	public boolean delete(Long id) {
		Optional<MediaResource> m = repo.findById(id);
		if (!m.isPresent()) {
			throw new InvalidInputDataException("Media resource can not be found");
		}
		
		try {
			boolean result = false;
			if (m.get().getResourceType().equals("video")) {
				result = cloudinaryService.deleteVideo(m.get().getPublicId());
			}
			else {
				result = cloudinaryService.delete(m.get().getPublicId());
			}
			
			if (result) {
				repo.delete(m.get());
				return true;
			}
			else {
				return false;
			}
		} catch (IOException e) {
			throw new InvalidInputDataException("IOException occurred when delete data from Cloudinary service (" + e.getMessage() + ")");
		}
	}
}
