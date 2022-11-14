package com.lucistore.lucistorebe.service;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.BuyerDeliveryAddressDTO;
import com.lucistore.lucistorebe.controller.payload.request.buyerdeliveryaddress.CreateBuyerDeliveryAddressRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerdeliveryaddress.UpdateBuyerDeliveryAddressRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress_;
import com.lucistore.lucistorebe.repo.BuyerDeliveryAddressRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.address.AddressWardRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EBuyerDeliveryAddressStatus;


@Service
public class BuyerDeliveryAddressService {
	@Autowired 
	BuyerDeliveryAddressRepo buyerDeliveryAddressRepo;
	
	@Autowired 
	BuyerRepo buyerRepo;

    @Autowired
    AddressWardRepo addressWardRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<BuyerDeliveryAddressDTO> get(Long id, Long idBuyer) {
        BuyerDeliveryAddress deliveryAddress = buyerDeliveryAddressRepo.findById(id).orElseThrow(
                () -> new InvalidInputDataException("No Delivery Address found with given id " + id));
        
        checkAvailable(deliveryAddress, idBuyer);

		return serviceUtils.convertToDataResponse(deliveryAddress, BuyerDeliveryAddressDTO.class);
	}
	
	public ListResponse<BuyerDeliveryAddressDTO> getAllByIdBuyer(Long idBuyer, Boolean isAdmin) {
		if(!buyerRepo.existsById(idBuyer)) {
			throw new InvalidInputDataException("No Buyer found with given id " + idBuyer);
		}
		
		Buyer buyer = buyerRepo.getReferenceById(idBuyer);

        List<BuyerDeliveryAddress> deliveryAddresses = buyerDeliveryAddressRepo.findAllByBuyer(buyer, Sort.by(Sort.Direction.ASC, BuyerDeliveryAddress_.RECEIVER_NAME));

        if(!isAdmin) {
            deliveryAddresses.removeIf(deliveryAddress -> deliveryAddress.getStatus() == EBuyerDeliveryAddressStatus.DELETED);
        }

        return serviceUtils.convertToListResponse(deliveryAddresses,BuyerDeliveryAddressDTO.class);
	}
	
	public DataResponse<BuyerDeliveryAddressDTO> create(Long idBuyer, CreateBuyerDeliveryAddressRequest data) {

        if(!addressWardRepo.existsById(data.getIdAddressWard())) {
            throw new InvalidInputDataException("No Address Ward found with given id " + data.getIdAddressWard());
        }

        Buyer buyer = buyerRepo.getReferenceById(idBuyer);

        if(data.getReceiverName() == null || data.getReceiverName().isEmpty()) {
            data.setReceiverName(buyer.getUser().getFullname());
        }
        if(data.getReceiverPhone() == null || data.getReceiverName().isEmpty()) {
            data.setReceiverName(buyer.getUser().getPhone());
        }
        
		BuyerDeliveryAddress deliveryAddress = new BuyerDeliveryAddress(
                buyer,
                addressWardRepo.getReferenceById(data.getIdAddressWard()),
                data.getAddressDetail(),
                data.getReceiverName(),
                data.getReceiverPhone()
		);

        if(data.getIsDefault()) {
            buyer.setDefaultAddress(deliveryAddress);
        }
		
		return serviceUtils.convertToDataResponse(buyerDeliveryAddressRepo.save(deliveryAddress), BuyerDeliveryAddressDTO.class);
	}
	
	public DataResponse<BuyerDeliveryAddressDTO> update(Long id, Long idBuyer, UpdateBuyerDeliveryAddressRequest data) {
		BuyerDeliveryAddress deliveryAddress = buyerDeliveryAddressRepo
				.findById(id).orElseThrow(
						() -> new InvalidInputDataException("No Delivery Address found with given id " + id));

        checkAvailable(deliveryAddress, idBuyer);

        if(!addressWardRepo.existsById(data.getIdAddressWard())) {
            throw new InvalidInputDataException("No Address Ward found with given id " + data.getIdAddressWard());
        }

        deliveryAddress.setAddressWard(addressWardRepo.getReferenceById(data.getIdAddressWard()));
        deliveryAddress.setAddressDetail(data.getAddressDetail());
        deliveryAddress.setReceiverName(data.getReceiverName());
        deliveryAddress.setReceiverPhone(data.getReceiverPhone());
        if(data.getIsDefault()) {
            deliveryAddress.getBuyer().setDefaultAddress(deliveryAddress);
        }

		return serviceUtils.convertToDataResponse(buyerDeliveryAddressRepo.save(deliveryAddress), BuyerDeliveryAddressDTO.class);
	}

    public DataResponse<BuyerDeliveryAddressDTO> delete(Long id, Long idBuyer) {
        BuyerDeliveryAddress deliveryAddress = buyerDeliveryAddressRepo
                .findById(id).orElseThrow(
                        () -> new InvalidInputDataException("No Delivery Address found with given id " + id));

        if (idBuyer != null) {
            checkAvailable(deliveryAddress, idBuyer);

            if (deliveryAddress.getBuyer().getDefaultAddress().getId().equals(id)) {
                throw new InvalidInputDataException(MessageFormat.format(
                        "Cannot delete the default delivery address. Buyer with id {0} must add another one as default delivery address.", idBuyer));
            }
        }

        deliveryAddress.setStatus(EBuyerDeliveryAddressStatus.DELETED);

        return serviceUtils.convertToDataResponse(buyerDeliveryAddressRepo.save(deliveryAddress),
                BuyerDeliveryAddressDTO.class);
    }

    private void checkAvailable(BuyerDeliveryAddress deliveryAddress, Long idBuyer) {
        if(!deliveryAddress.getBuyer().getId().equals(idBuyer)) {
            throw new InvalidInputDataException(
                MessageFormat.format("No Delivery Address found with given id {0} for buyer with id {1} ", deliveryAddress.getId(), idBuyer));
        }
        if(deliveryAddress.getStatus() == EBuyerDeliveryAddressStatus.DELETED) {
            throw new InvalidInputDataException("Delivery Address has been deleted");
        }
    }
}
