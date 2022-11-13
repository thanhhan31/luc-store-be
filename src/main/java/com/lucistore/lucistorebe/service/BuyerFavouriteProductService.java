package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerFavouriteProductDTO;
import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.pk.BuyerFavouriteProductPK;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerFavouriteProduct;
import com.lucistore.lucistorebe.repo.BuyerFavouriteProductRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EProductStatus;


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
		BuyerFavouriteProduct favouriteProduct = buyerFavouriteProductRepo.findById(
				new BuyerFavouriteProductPK(idBuyer, id)).orElseThrow(
						() -> new InvalidInputDataException("No buyer favorite product found with given id " + id));
		
		checkAvailable(favouriteProduct);

		return serviceUtils.convertToDataResponse(favouriteProduct, BuyerFavouriteProductDTO.class);
	}
	
	public ListResponse<BuyerFavouriteProductDTO> getAllByIdBuyer(Long idBuyer) {
		if(!buyerRepo.existsById(idBuyer)) {
			throw new InvalidInputDataException("No Buyer found with given id " + idBuyer);
		}
		
		Buyer buyer = buyerRepo.getReferenceById(idBuyer);

		return serviceUtils.convertToListResponse(
				buyerFavouriteProductRepo.findAllByBuyer(buyer, null).stream().filter(
						fp -> fp.getProduct().getStatus() == EProductStatus.ENABLED).toList(),
				BuyerFavouriteProductDTO.class);
	}
	
	public DataResponse<BuyerFavouriteProductDTO> create(Long idProduct, Long idBuyer) {
		if(!productRepo.existsById(idProduct)) {
			throw new InvalidInputDataException("No Product found with given id " + idProduct);
		}

		BuyerFavouriteProduct favouriteProduct = new BuyerFavouriteProduct(
				buyerRepo.getReferenceById(idBuyer), productRepo.getReferenceById(idProduct));

		checkAvailable(favouriteProduct);
		
		return serviceUtils.convertToDataResponse(buyerFavouriteProductRepo.save(favouriteProduct), BuyerFavouriteProductDTO.class);
	}

	public BaseResponse delete(Long idProduct, Long idBuyer) {
		var id = new BuyerFavouriteProductPK(idBuyer, idProduct);
		if(!buyerFavouriteProductRepo.existsById(id)){
			throw new InvalidInputDataException("No Buyer favourite product found with given id " + idProduct);
		}

		buyerFavouriteProductRepo.deleteById(id);

		return new BaseResponse();
	}

	private void checkAvailable(BuyerFavouriteProduct favouriteProduct) {
		if (favouriteProduct.getProduct().getStatus() != EProductStatus.ENABLED) {
			throw new InvalidInputDataException(
					"Product with id " + favouriteProduct.getProduct().getId() + " is not available");
		}
	}
}
