package com.lucistore.lucistorebe.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerCartDetailDTO;
import com.lucistore.lucistorebe.controller.payload.request.buyercartdetail.CreateBuyerCartDetailRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyercartdetail.UpdateBuyerCartDetailRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerCartDetail;
import com.lucistore.lucistorebe.repo.BuyerCartDetailRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;


@Service
public class BuyerCartDetailService {
	@Autowired 
	BuyerCartDetailRepo buyerCartDetailRepo;
	
	@Autowired 
	BuyerRepo buyerRepo;

	@Autowired
	ProductVariationRepo productVariationRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<BuyerCartDetailDTO> get(Long idProductVariation, Long idBuyer) {
		BuyerCartDetail cartDetail = buyerCartDetailRepo.findById(
				new BuyerCartDetailPK(idBuyer, idProductVariation)).orElseThrow(
						() -> new InvalidInputDataException("No Cart Detail found with given id " + idProductVariation));

		return serviceUtils.convertToDataResponse(cartDetail, BuyerCartDetailDTO.class);
	}
	
	public ListResponse<BuyerCartDetailDTO> getAllByIdBuyer(Long idBuyer) {
		if(!buyerRepo.existsById(idBuyer)) {
			throw new InvalidInputDataException("No Buyer found with given id " + idBuyer);
		}
		
		Buyer buyer = buyerRepo.getReferenceById(idBuyer);

		return serviceUtils.convertToListResponse(buyerCartDetailRepo.findAllByBuyer(buyer, Sort.by("productVariation.product")), BuyerCartDetailDTO.class);
	}
	
	public DataResponse<BuyerCartDetailDTO> create(Long idProductVariation, Long idBuyer, CreateBuyerCartDetailRequest data) {
		if(!productVariationRepo.existsById(idProductVariation)) {
			throw new InvalidInputDataException("No Product Variation found with given id " + idProductVariation);
		}

		BuyerCartDetail cartDetail = new BuyerCartDetail(
			buyerRepo.getReferenceById(idBuyer),
			productVariationRepo.getReferenceById(idProductVariation),
			data.getQuantity()
		);
		
		return serviceUtils.convertToDataResponse(buyerCartDetailRepo.save(cartDetail), BuyerCartDetailDTO.class);
	}
	
	public DataResponse<BuyerCartDetailDTO> update(Long idProductVariation, Long idBuyer, UpdateBuyerCartDetailRequest data) {
		BuyerCartDetail cartDetail = buyerCartDetailRepo
				.findById(new BuyerCartDetailPK(idBuyer, idProductVariation)).orElseThrow(
						() -> new InvalidInputDataException("No Cart Detail found with given id " + idProductVariation));

		cartDetail.setQuantity(data.getQuantity());
		
		return serviceUtils.convertToDataResponse(buyerCartDetailRepo.save(cartDetail), BuyerCartDetailDTO.class);
	}
}
