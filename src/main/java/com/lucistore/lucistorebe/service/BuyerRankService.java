package com.lucistore.lucistorebe.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerDTO;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerRankDTO;
import com.lucistore.lucistorebe.controller.payload.request.buyerrank.UpdateBuyerRankRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerRank;
import com.lucistore.lucistorebe.repo.BuyerRankRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;

@Service
public class BuyerRankService {
	@Autowired 
	BuyerRankRepo buyerRankRepo;
	
	@Autowired 
	BuyerRepo buyerRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<BuyerRankDTO> getById(Long id) {
		BuyerRank buyerRank = buyerRankRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No buyer rank found with given id")
			);
		
		return serviceUtils.convertToDataResponse(buyerRank, BuyerRankDTO.class);
	}
	
	public ListResponse<BuyerRankDTO> getAll() {
		return serviceUtils.convertToListResponse(buyerRankRepo.findAll(), BuyerRankDTO.class);
	}
	
	public DataResponse<BuyerRankDTO> update(Long id, @Valid UpdateBuyerRankRequest data) {
		BuyerRank buyerRank = buyerRankRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No Buyer Rank found with given id")
			);
		
			buyerRank.setDiscountRate(data.getDiscountRate());
		
		return serviceUtils.convertToDataResponse(buyerRankRepo.save(buyerRank), BuyerRankDTO.class);
	}

	public DataResponse<BuyerDTO> rankUp(Long idBuyer) {
		Buyer buyer = buyerRepo.findById(idBuyer).orElseThrow(
				() -> new InvalidInputDataException("No Buyer found with given id")
			);
		
		if (buyer.getRank().getNextRank() == null) {
			throw new InvalidInputDataException("Buyer already has the highest rank");
		}

		if (buyer.getPoint() < buyer.getRank().getThreshold()) {
			throw new InvalidInputDataException("Buyer doesn't have enough point to rank up");
		}

		buyer.setRank(buyer.getRank().getNextRank());
		
		return serviceUtils.convertToDataResponse(buyerRepo.save(buyer), BuyerDTO.class);
	}
}
