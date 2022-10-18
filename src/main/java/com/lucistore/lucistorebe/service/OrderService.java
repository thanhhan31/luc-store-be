package com.lucistore.lucistorebe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.OrderDTO;
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderFromCartRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderFromProductRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.OrderDetail;
import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerCartDetail;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress;
import com.lucistore.lucistorebe.repo.BuyerCartDetailRepo;
import com.lucistore.lucistorebe.repo.BuyerDeliveryAddressRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.OrderRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.EProductVariationStatus;


@Service
public class OrderService {
	@Autowired 
	OrderRepo orderRepo;
	
	@Autowired 
	BuyerRepo buyerRepo;

	@Autowired
	BuyerDeliveryAddressRepo buyerDeliveryAddressRepo;

	@Autowired
	ProductVariationRepo productVariationRepo;

	@Autowired
	BuyerCartDetailRepo buyerCartDetailRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<OrderDTO> get(Long id, Long idBuyer) {
		Order order = orderRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No Order found with given id " + id));

		if(idBuyer != null && !order.getBuyer().getId().equals(idBuyer)) {
			throw new InvalidInputDataException("No Order found with given id " + id + " for given buyer");
		}
		
		return serviceUtils.convertToDataResponse(order, OrderDTO.class);
	}
	
	public ListResponse<OrderDTO> getAllByIdBuyer(Long idBuyer) {
		if(!buyerRepo.existsById(idBuyer)) {
			throw new InvalidInputDataException("No Buyer found with given id " + idBuyer);
		}
		
		return serviceUtils.convertToListResponse(orderRepo.findAllByBuyerId(idBuyer, Sort.by("productVariation.product")), OrderDTO.class);
	}
	
	public DataResponse<OrderDTO> create(Long idBuyer, CreateBuyerOrderRequest data) {

		BuyerDeliveryAddress address = buyerDeliveryAddressRepo.getReferenceById(data.getIdAddress());
		Buyer buyer = buyerRepo.getReferenceById(idBuyer);

		if(!buyerRepo.existsById(idBuyer)) {
			throw new InvalidInputDataException("No Buyer found with given id " + idBuyer);
		}

		if(!buyerDeliveryAddressRepo.existsById(data.getIdAddress())
				|| !address.getBuyer().getId().equals(idBuyer)) {
			throw new InvalidInputDataException("No Buyer Delivery Address found with given id " + data.getIdAddress());
		}

		Order order = new Order(
			buyer,
			address,
			data.getNote(),
			EOrderStatus.WAIT_FOR_CONFIRM,
			data.getPaymentMethod()
		);

		List<BuyerCartDetail> cartDetails = getBuyerCartDetails(buyer, data);

		cartDetails.stream().forEach(cd -> {
			OrderDetail orderDetail = createOrderDetail(cd, order);
			order.getOrderDetails().add(orderDetail);
			order.setPrice(order.getPrice() + orderDetail.getUnitPrice() * orderDetail.getQuantity());
		});
		
		return serviceUtils.convertToDataResponse(orderRepo.save(order), OrderDTO.class);
	}

	public DataResponse<OrderDTO> cancel(Long id, Long idBuyer) {
		Order order = orderRepo
				.findById(id).orElseThrow(
						() -> new InvalidInputDataException("No Order found with given id " + id));

		if(idBuyer != null && !order.getBuyer().getId().equals(idBuyer)) {
			throw new InvalidInputDataException("No Order found with given id " + id);
		}

		order.setStatus(EOrderStatus.CANCELLED);
		
		return serviceUtils.convertToDataResponse(orderRepo.save(order), OrderDTO.class);
	}

	private List<BuyerCartDetail> getBuyerCartDetails(Buyer buyer, CreateBuyerOrderRequest data) {

		List<Long> idps = new ArrayList<>();
		List<BuyerCartDetail> cartDetails = new ArrayList<>();

		if (data.getClass().equals(CreateBuyerOrderFromCartRequest.class)) {
			idps.addAll(
				((CreateBuyerOrderFromCartRequest) data).getIdProductVariations()
			);
			
			cartDetails = buyerCartDetailRepo.findAllById(
					idps.stream().map(id -> new BuyerCartDetailPK(buyer.getId(), id)).collect(Collectors.toList())
			);

			List<Long> idpvFounds = cartDetails.stream().map(cd -> cd.getProductVariation().getId()).collect(Collectors.toList());

			idps.stream().forEach(id -> {
				if(!idpvFounds.contains(id)) {
					throw new InvalidInputDataException("No Product Variation found with given id " + id);
				}
			});

		} else if (data.getClass().equals(CreateBuyerOrderFromProductRequest.class)) {
			idps.add(((CreateBuyerOrderFromProductRequest) data).getIdProductVariation());

			cartDetails.add(
				new BuyerCartDetail(
					buyer,
					productVariationRepo.findById(idps.get(0)).orElseThrow(
						() -> new InvalidInputDataException("No Product Variation found with given id " + idps.get(0))
					),
					((CreateBuyerOrderFromProductRequest) data).getQuantity()
				)
			);
		}


		return cartDetails;
	}

	private OrderDetail createOrderDetail(BuyerCartDetail cartDetail, Order order) {

		checkAvailable(cartDetail);

		return new OrderDetail(
			order,
			cartDetail.getProductVariation(),
			cartDetail.getQuantity(),
			cartDetail.getProductVariation().getPrice()
		);
	}

	private void checkAvailable(BuyerCartDetail cartDetail) {
		if (cartDetail.getProductVariation().getStatus() != EProductVariationStatus.ENABLED) {
			throw new InvalidInputDataException(
					"Product Variation with id " + cartDetail.getProductVariation().getId() + " is not available");
		}

		if (cartDetail.getProductVariation().getProduct().getStatus() != EProductStatus.ENABLED) {
			throw new InvalidInputDataException(
					"Product with id " + cartDetail.getProductVariation().getProduct().getId() + " is not available");
		}

		if (cartDetail.getProductVariation().getAvailableQuantity() < cartDetail.getQuantity()) {
			throw new InvalidInputDataException(
					"Stock is not enough for product variation with id " + cartDetail.getProductVariation().getId());
		}
	}
}
