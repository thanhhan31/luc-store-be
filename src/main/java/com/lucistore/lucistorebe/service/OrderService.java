package com.lucistore.lucistorebe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.CreateOrderDTO;
import com.lucistore.lucistorebe.controller.payload.dto.OrderDTO;
import com.lucistore.lucistorebe.controller.payload.dto.order.AdminDetailedOrderDTO;
import com.lucistore.lucistorebe.controller.payload.dto.order.AdminOrderDTO;
import com.lucistore.lucistorebe.controller.payload.dto.order.BuyerDetailedOrderDTO;
import com.lucistore.lucistorebe.controller.payload.dto.order.BuyerOrderDTO;
import com.lucistore.lucistorebe.controller.payload.request.BuyerUpdateOrderPaymentMethodRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderFromCartRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderFromProductRequest;
import com.lucistore.lucistorebe.controller.payload.request.buyerorder.CreateBuyerOrderRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.OrderDetail;
import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerCartDetail;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress;
import com.lucistore.lucistorebe.repo.BuyerCartDetailRepo;
import com.lucistore.lucistorebe.repo.BuyerDeliveryAddressRepo;
import com.lucistore.lucistorebe.repo.BuyerRepo;
import com.lucistore.lucistorebe.repo.OrderDetailRepo;
import com.lucistore.lucistorebe.repo.OrderRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EBuyerDeliveryAddressStatus;
import com.lucistore.lucistorebe.utility.EOrderStatus;
import com.lucistore.lucistorebe.utility.EPaymentMethod;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.EProductVariationStatus;
import com.lucistore.lucistorebe.utility.filter.OrderFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

@Service
public class OrderService {
	@Autowired 
	OrderRepo orderRepo;
	
	@Autowired
	OrderDetailRepo orderDetailRepo;
	
	@Autowired 
	BuyerRepo buyerRepo;

	@Autowired 
	PaymentService paymentService;

	@Autowired
	BuyerDeliveryAddressRepo buyerDeliveryAddressRepo;

	@Autowired
	ProductVariationRepo productVariationRepo;

	@Autowired
	BuyerCartDetailRepo buyerCartDetailRepo;

	@Autowired
	BuyerRankService buyerRankService;
	
	@Autowired
	ServiceUtils serviceUtils;

	public ListWithPagingResponse<?> search(OrderFilter filter, PagingInfo pagingInfo, boolean isBuyer) {
		if (isBuyer) {
			return serviceUtils.convertToListResponse(
				orderRepo.search(filter, pagingInfo),
				BuyerOrderDTO.class
			);
		}
		else {
			return serviceUtils.convertToListResponse(
				orderRepo.search(filter, pagingInfo),
				AdminOrderDTO.class
			);
		}
	}
	
	public DataResponse<?> get(Long id, Long idBuyer) {
		Order order = orderRepo.findById(id).orElseThrow(
				() -> new InvalidInputDataException("No order found with given id"));

		if(idBuyer != null && !order.getBuyer().getId().equals(idBuyer)) 
			throw new InvalidInputDataException("Can not read order of other buyers");
		
		if (idBuyer != null) {
			return serviceUtils.convertToDataResponse(order, BuyerDetailedOrderDTO.class);
		}
		else {
			return serviceUtils.convertToDataResponse(order, AdminDetailedOrderDTO.class);
		}
	}
	
	@Transactional
	public DataResponse<CreateOrderDTO> create(Long idBuyer, CreateBuyerOrderRequest data) {

		BuyerDeliveryAddress address = buyerDeliveryAddressRepo.getReferenceById(data.getIdAddress());
		Buyer buyer = buyerRepo.getReferenceById(idBuyer);

		if(Boolean.FALSE.equals(buyer.getPhoneConfirmed()))
			throw new CommonRuntimeException("Please confirm your phone number before placing order");

		if(!buyerRepo.existsById(idBuyer)) {
			throw new InvalidInputDataException("No Buyer found with given id " + idBuyer);
		}

		if(!buyerDeliveryAddressRepo.existsById(data.getIdAddress())
				|| !address.getBuyer().getId().equals(idBuyer)
				|| !address.getStatus().equals(EBuyerDeliveryAddressStatus.ACTIVE)) {
			throw new InvalidInputDataException("No Buyer Delivery Address found with given id " + data.getIdAddress());
		}

		Order order = new Order(
			buyer,
			address,
			data.getNote(),
			EOrderStatus.WAIT_FOR_PAYMENT,
			data.getPaymentMethod()
		);

		List<BuyerCartDetail> cartDetails = getBuyerCartDetails(buyer, data);

		cartDetails.stream().forEach(cd -> {
			OrderDetail orderDetail = createOrderDetail(cd, order);
			order.getOrderDetails().add(orderDetail);
			order.setPrice(order.getPrice() + orderDetail.getUnitPrice() * orderDetail.getQuantity());
		});

		order.setPrice(Math.round(order.getPrice()*(1 - buyer.getRank().getDiscountRate()))); // apply discount from rank
		order.setPayPrice(order.getPrice() + 30000L); // shipping fee
		
		return serviceUtils.convertToDataResponse(orderRepo.save(order), CreateOrderDTO.class);
	}
	
	public void checkAndCompleteOrder(Long id) {
		if (!orderDetailRepo.existsByOrderAndReviewed(orderRepo.getReferenceById(id), false)) {
			Order order = orderRepo.getReferenceById(id);
			order.setStatus(EOrderStatus.COMPLETED);
			orderRepo.save(order);
		}
	}
	
	@Transactional
	public DataResponse<BuyerOrderDTO> updatePaymentMethod(Long id, Long idBuyer, BuyerUpdateOrderPaymentMethodRequest data) {
		Order order = orderRepo.findById(id).orElseThrow(
			() -> new InvalidInputDataException("No order found with given id "));
		
		if(idBuyer != null && !order.getBuyer().getId().equals(idBuyer))
			throw new InvalidInputDataException("Can not update other buyer's orders");
		
		
		if (order.getStatus() != EOrderStatus.WAIT_FOR_PAYMENT)
			throw new InvalidInputDataException("Payment method can only be updated when order is in wait for payment state");
		
		if (order.getPaymentMethod() != data.getNewPaymentMethod()) {
			order.setPaymentMethod(data.getNewPaymentMethod());
			order = orderRepo.save(order);
		}
		
		return serviceUtils.convertToDataResponse(order, BuyerOrderDTO.class);
	}
	
	@Transactional
	public DataResponse<OrderDTO> updateStatus(Long id, Long idBuyer, EOrderStatus newStatus) {
		Order order = orderRepo.findById(id).orElseThrow(
			() -> new InvalidInputDataException("No order found with given id "));
		
		if(idBuyer != null && !order.getBuyer().getId().equals(idBuyer))
			throw new InvalidInputDataException("Can not update other buyer's orders");
		
		if (newStatus == EOrderStatus.CANCELLED)
			return cancelOrder(order);

		if (newStatus == EOrderStatus.WAIT_FOR_SEND && order.getStatus() == EOrderStatus.WAIT_FOR_CONFIRM) {
			order.getOrderDetails().stream().forEach(od -> {
				if(productVariationRepo.reduceStock(od.getProductVariation().getId(), od.getQuantity()) == 0 ){
					throw new InvalidInputDataException("Product variation " + od.getProductVariation().getId() + " is out of stock");
				}
			});
		}

		if(newStatus == EOrderStatus.COMPLETED) {
			Long totalSpend = order.getBuyer().getTotalSpent() == null ? order.getPayPrice() : order.getBuyer().getTotalSpent() + order.getPayPrice();
			Buyer buyer = order.getBuyer();
			buyer.setTotalSpent(totalSpend);
			if(buyer.getRank().getNextRank() != null && buyer.getTotalSpent() >= buyer.getRank().getThreshold()) {
				buyerRankService.rankUp(buyerRepo.save(buyer).getId());
			}else{
				buyerRepo.save(buyer);
			}
		}
		
		order.setStatus(newStatus);
		return serviceUtils.convertToDataResponse(orderRepo.save(order), OrderDTO.class);
	}

	@Transactional
	private DataResponse<OrderDTO> cancelOrder(Order order) {
		switch (order.getStatus()) {
			case WAIT_FOR_CONFIRM:
				if (order.getPaymentMethod().equals(EPaymentMethod.ONLINE_PAYMENT_MOMO)
						|| order.getPaymentMethod().equals(EPaymentMethod.ONLINE_PAYMENT_PAYPAL)) {
					paymentService.refundPayment(order);
				}
				order.setStatus(EOrderStatus.CANCELLED);
				break;
			case WAIT_FOR_SEND:
				if (order.getPaymentMethod().equals(EPaymentMethod.ONLINE_PAYMENT_MOMO)
						|| order.getPaymentMethod().equals(EPaymentMethod.ONLINE_PAYMENT_PAYPAL)) {
					paymentService.refundPayment(order);
				}
				
				// revert stock when order is cancelled
				order.getOrderDetails().stream().forEach(od -> 
					productVariationRepo.refundStock(od.getProductVariation().getId(), od.getQuantity())
				);

				order.setStatus(EOrderStatus.CANCELLED);
				break;
			case WAIT_FOR_PAYMENT:
				order.setStatus(EOrderStatus.CANCELLED);
				break;
			default:
				throw new CommonRuntimeException("Order cannot be cancelled");
		}
		
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

			buyerCartDetailRepo.deleteAllInBatch(cartDetails); // delete cart item from cart

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
			(long)Math.ceil(cartDetail.getProductVariation().getPrice() * (1 - cartDetail.getProductVariation().getDiscount() / 100.0))
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
