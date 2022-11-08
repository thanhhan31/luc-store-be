package com.lucistore.lucistorebe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.entity.MediaResource;
import com.lucistore.lucistorebe.entity.product.ProductReview;
import com.lucistore.lucistorebe.entity.product.ProductReviewImage;
import com.lucistore.lucistorebe.repo.ProductReviewImageRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;

@Service
public class ProductReviewImageService {
	@Autowired
	ProductReviewImageRepo productReviewImageRepo;
	
	@Autowired
	MediaResourceService mediaResourceService;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	@Transactional
	public void create(ProductReview pr, List<MultipartFile> images) {
		List<MediaResource> mrs = new ArrayList<>();
		List<ProductReviewImage> primgs = new ArrayList<>();
		
		try {
			for (MultipartFile image : images) {
				MediaResource mr = mediaResourceService.save(image.getBytes());
				mrs.add(mr);
				primgs.add(
						productReviewImageRepo.save(
							new ProductReviewImage(
									pr, 
									mr
								)
						)
					);
			}
		} catch (IOException e) {
			for (MediaResource mr : mrs) {
				mediaResourceService.delete(mr);
			}
			throw new CommonRuntimeException("Error occurred when trying to save product review image");
		}
	}
}
