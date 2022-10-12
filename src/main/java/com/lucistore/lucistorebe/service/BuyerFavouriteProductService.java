package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerFavouriteProductDTO;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.pk.BuyerFavouriteProductPK;
import com.lucistore.lucistorebe.entity.product.Product_;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerFavouriteProduct;
import com.lucistore.lucistorebe.repo.BuyerFavouriteProductRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;


@Service
public class BuyerFavouriteProductService {
	@Autowired 
	BuyerFavouriteProductRepo buyerFavouriteProductRepo;
	
	@Autowired 
	BuyerRepo buyerRepo;

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<BuyerFavouriteProductDTO> get(Long id, Long idBuyer) {
		BuyerFavouriteProduct FavouriteProduct = buyerFavouriteProductRepo.findById(
				new BuyerFavouriteProductPK(idBuyer, id)).orElseThrow(
						() -> new InvalidInputDataException("No Cart Detail found with given id " + id));

		return serviceUtils.convertToDataResponse(FavouriteProduct, BuyerFavouriteProductDTO.class);
	}
	
	public ListResponse<BuyerFavouriteProductDTO> getAllByIdBuyer(Long idBuyer) {
		if(!buyerRepo.existsById(idBuyer)) {
			throw new InvalidInputDataException("No Buyer found with given id " + idBuyer);
		}
		
		Buyer buyer = buyerRepo.getReferenceById(idBuyer);

		return serviceUtils.convertToListResponse(buyerFavouriteProductRepo.findAllByBuyer(buyer, Sort.by(Product_.NAME)), BuyerFavouriteProductDTO.class);
	}
	
	public DataResponse<BuyerFavouriteProductDTO> create(Long idProduct, Long idBuyer) {
		if(!productRepo.existsById(idProduct)) {
			throw new InvalidInputDataException("No Product found with given id " + idProduct);
		}

		BuyerFavouriteProduct FavouriteProduct = new BuyerFavouriteProduct(
				buyerRepo.getReferenceById(idBuyer), productRepo.getReferenceById(idProduct));
		
		return serviceUtils.convertToDataResponse(buyerFavouriteProductRepo.save(FavouriteProduct), BuyerFavouriteProductDTO.class);
	}
}
