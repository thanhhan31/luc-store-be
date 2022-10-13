package com.lucistore.lucistorebe.service.thirdparty;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lucistore.lucistorebe.controller.payload.response.service.CloudinaryUploadResponse;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Service
@ConfigurationProperties(prefix = "com.lucistore.lucistorebe.service.cloudinary")
public class CloudinaryService {
	private String cloudName;
	private String apiKey;
	private String apiSecret;
    
    private Cloudinary cloudinary;

    @PostConstruct
    private void init() {
    	cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret,
				"secure", true));
    }
    
    public CloudinaryUploadResponse upload(byte[] data) throws IOException {    	
    	Map response = cloudinary.uploader().upload(
    			data,
    			ObjectUtils.asMap(
    					"resource_type", "auto",
    					"folder", "luc-store")
    		);
    	
    	return new CloudinaryUploadResponse(
    			response.get("secure_url").toString(),
    			response.get("public_id").toString(),
    			response.get("resource_type").toString());
    }
    
    public boolean delete(String publicId) throws IOException {
        Map response = cloudinary.uploader().destroy(
        		publicId, 
        		ObjectUtils.emptyMap()
        	);
        
        return response.get("result").toString().equals("ok");
    }
    
    public boolean deleteVideo(String publicId) throws IOException {
        Map response = cloudinary.uploader().destroy(
        		publicId, 
        		ObjectUtils.asMap("resource_type", "video")
        	);
        
        return response.get("result").toString().equals("ok");
    }
}
