package com.lucistore.lucistorebe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.ProductReviewDTO;
import com.lucistore.lucistorebe.controller.payload.request.BuyerCreateProductReviewRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.order.OrderDetailPK;
import com.lucistore.lucistorebe.entity.product.ProductReview;
import com.lucistore.lucistorebe.entity.product.ProductReview_;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.OrderDetailRepo;
import com.lucistore.lucistorebe.repo.ProductReviewRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EOrderStatus;

@Service
public class ProductReviewService {
	@Autowired
	ProductReviewRepo productReviewRepo;
	
	@Autowired
	ProductVariationRepo productVariationRepo;
	
	@Autowired
	OrderDetailRepo orderDetailRepo;
	
	@Autowired
	BuyerRepo buyerRepo;
	
	@Autowired 
	OrderService orderService;
	
	@Autowired
	ProductReviewImageService productReviewImageService;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	@Transactional
	public DataResponse<ProductReviewDTO> create(Long idBuyer, BuyerCreateProductReviewRequest data, List<MultipartFile> images) {
		var od = orderDetailRepo.findById(new OrderDetailPK(data.getIdOrder(), data.getIdProductVariation())).orElseThrow(
				() -> new InvalidInputDataException("Order does not contain given product variation or no order found")
			);
		
		if (od.getOrder().getStatus() != EOrderStatus.DELIVERED)
			throw new InvalidInputDataException("Can not review order which is in processing state");
		
		if (!od.getOrder().getBuyer().getId().equals(idBuyer))
			throw new InvalidInputDataException("Can not review order of other users");
		
		if (od.getReviewed().booleanValue())
			throw new InvalidInputDataException("This product of this order has been reviewed!");
		
		var pr = productReviewRepo.save(
				new ProductReview(
					buyerRepo.getReferenceById(idBuyer), 
					productVariationRepo.getReferenceById(data.getIdProductVariation()),
					data.getPoint(), 
					data.getContent()
				)
			);
		
		if (images != null)
			productReviewImageService.create(pr, images);
		
		orderService.checkAndCompleteOrder(od.getOrder().getId());
		productReviewRepo.refresh(pr);

		return serviceUtils.convertToDataResponse(pr, ProductReviewDTO.class);
	}
	
	public ListWithPagingResponse<ProductReviewDTO> getReviews(Long idProduct, Integer page, Integer size, Boolean sortByOldest) {
		PageRequest pr = serviceUtils.getPageRequest(page, size);
		
		if (sortByOldest != null && sortByOldest.booleanValue())
			pr = pr.withSort(Sort.by(ProductReview_.TIME).descending()); //sort by lasted
		else
			pr = pr.withSort(Sort.by(ProductReview_.TIME).ascending()); //sort by oldest
		
		return serviceUtils.convertToListResponse(
				productReviewRepo.findByIdProduct(idProduct, pr), 
				ProductReviewDTO.class
			);
	}
}
