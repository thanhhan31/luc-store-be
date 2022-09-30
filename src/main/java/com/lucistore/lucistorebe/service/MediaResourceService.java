package com.lucistore.lucistorebe.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.response.service.CloudinaryUploadResponse;
import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.repo.MediaResourceRepo;

@Service
public class MediaResourceService {
	@Autowired
	MediaResourceRepo repo;
	
	@Autowired
	CloudinaryService cloudinaryService;

	@SuppressWarnings("unused")
	@Transactional
	public MediaResource save(byte[] data) {
		CloudinaryUploadResponse resp = null;
		try {
			resp =  cloudinaryService.upload(data);
			MediaResource m = new MediaResource(
					resp.getUrl(), 
					resp.getPublicId(),
					resp.getResourceType());
			return repo.save(m);
		} catch (IOException e) {
			if (resp != null) { //resource has been uploaded to 3rd service
				try {
					if (resp.getResourceType().equals("video"))
						cloudinaryService.deleteVideo(resp.getPublicId());
					else
						cloudinaryService.delete(resp.getPublicId());
				} catch (IOException e2) {
					throw new InvalidInputDataException("IOException occurred when rollback from Cloudinary service (" + e.getMessage() + ")");
				}
			}
			throw new InvalidInputDataException("IOException occurred when upload data to Cloudinary service (" + e.getMessage() + ")");
		}
	}
	
	public void delete(Long id) {
		MediaResource m = repo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("Media resource can not be found")
			);
		delete(m);
	}
	
	public void delete(MediaResource m) {
		try {
			if (m.getResourceType().equals("video"))
				cloudinaryService.deleteVideo(m.getPublicId());
			else
				cloudinaryService.delete(m.getPublicId());
			repo.delete(m);
		} catch (IOException e) {
			throw new InvalidInputDataException("IOException occurred when delete data from Cloudinary service (" + e.getMessage() + ")");
		}
	}
}
