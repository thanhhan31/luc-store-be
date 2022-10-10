package com.lucistore.lucistorebe.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerCartDetailDTO;
import com.lucistore.lucistorebe.controller.payload.request.buyercartdetail.CreateBuyerCartDetailRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyercartdetail.UpdateBuyerCartDetailRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerCartDetail;
import com.lucistore.lucistorebe.repo.BuyerCartDetailRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;


@Service
public class CartDetailService {
	@Autowired 
	BuyerCartDetailRepo buyerCartDetailRepo;
	
	@Autowired 
	BuyerRepo buyerRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<BuyerCartDetailDTO> get(Long idProductVariation, Long idBuyer) {
		BuyerCartDetail cartDetail = buyerCartDetailRepo.findById(
				new BuyerCartDetailPK(idBuyer, idProductVariation)).orElseThrow(
						() -> new InvalidInputDataException("No Cart Detail found with given id" + idProductVariation));

		return serviceUtils.convertToDataResponse(cartDetail, BuyerCartDetailDTO.class);
	}
	
	public ListResponse<BuyerCartDetailDTO> getAllByIdBuyer(Long idBuyer) {
		return serviceUtils.convertToListResponse(buyerCartDetailRepo.findAllByIdBuyer(idBuyer), BuyerCartDetailDTO.class);
	}
	
	public DataResponse<BuyerCartDetailDTO> create(Long idBuyer, @Valid CreateBuyerCartDetailRequest data) {
		// Buyer buyer = buyerRepo.getReferenceById(idBuyer);

		BuyerCartDetail cartDetail = new BuyerCartDetail();
		cartDetail.setId(new BuyerCartDetailPK(idBuyer, data.getIdProductVariation()));
		cartDetail.setQuantity(data.getQuantity());
		
		return serviceUtils.convertToDataResponse(buyerCartDetailRepo.save(cartDetail), BuyerCartDetailDTO.class);
	}
	
	public DataResponse<BuyerCartDetailDTO> update(Long idBuyer, @Valid UpdateBuyerCartDetailRequest data) {
		BuyerCartDetail cartDetail = buyerCartDetailRepo
				.findById(new BuyerCartDetailPK(idBuyer, data.getIdProductVariation())).orElseThrow(
						() -> new InvalidInputDataException("No Cart Detail found with given id" + data.getIdProductVariation()));

		cartDetail.setQuantity(data.getQuantity());
		
		return serviceUtils.convertToDataResponse(buyerCartDetailRepo.save(cartDetail), BuyerCartDetailDTO.class);
	}
}
