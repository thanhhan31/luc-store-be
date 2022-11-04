package com.lucistore.lucistorebe.service.thirdparty.payment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.service.PaymentService;
import com.lucistore.lucistorebe.service.TransactionService;
import com.lucistore.lucistorebe.service.configuration.PayPalConfig;
import com.lucistore.lucistorebe.utility.PaypalOrderIdCache;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.payments.CapturesRefundRequest;
import com.paypal.payments.Refund;

@Service
public class PayPalService implements Payment {
	
	private PayPalHttpClient payPalHttpClient;
	
	@Autowired
	PaypalOrderIdCache cache;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	PayPalConfig payPalConfig;
	
	private final PaymentService paymentService;

	@PostConstruct
    public void init() {
        payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Sandbox(payPalConfig.getClientId(), payPalConfig.getClientSecret()));
    }
	
	public PayPalService(@Lazy PaymentService paymentService) {
		this.paymentService = paymentService;
	}
    
	@Override
	public String createPayment(Long idOrder, String amount, HttpServletRequest req) {
		OrderRequest orderRequest = buildOrderRequest(Utils.buildUrl(payPalConfig.getUrl().getCallback(), req));
		
		PurchaseUnitRequest purchaseUnitRequest = 
			new PurchaseUnitRequest()
	            .amountWithBreakdown(
            		new AmountWithBreakdown()
            		.currencyCode("USD")
            		.value(toUsd(amount))
	            );
		
	    orderRequest.purchaseUnits(Arrays.asList(purchaseUnitRequest));
	    OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);
	    HttpResponse<Order> orderHttpResponse = null;
	    
		try {
			orderHttpResponse = payPalHttpClient.execute(ordersCreateRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		if (orderHttpResponse != null) {
			Order order = orderHttpResponse.result();
				if (order.status().equals("CREATED"))  {
			    cache.create(order.id(), idOrder);
			    return getHrefByRel(order.links(), "approve");
			}
		}
		
	    throw new CommonRuntimeException("Error when create Paypal order");
	}
	
	@Override
	public void refundPayment(Long idOrder) {
		refundCapture(transactionService.getByIdOrder(idOrder).getData());
		transactionService.setRefund(idOrder);
	}
	
	public boolean captureOrder(String orderId) {
		OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(orderId);
		try {
			HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
			if (httpResponse.result().status().equals("COMPLETED")) {
				Long idOrder = cache.get(orderId);
				String captureId = httpResponse.result().purchaseUnits().get(0).payments().captures().get(0).id();
				if (paymentService.confirm(idOrder)) {
					transactionService.assignTransaction(idOrder, captureId);
					return true;
				}
				else {
					refundCapture(captureId);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void refundCapture(String captureId) {
		CapturesRefundRequest request = new CapturesRefundRequest(captureId);
		try {
			HttpResponse<Refund> response = payPalHttpClient.execute(request);
			if (!response.result().status().equals("COMPLETED")) {
				throw new CommonRuntimeException("Refund failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getHrefByRel(List<LinkDescription> links, String rel) {
		return links
			.stream()
			.filter(link -> link.rel().equals(rel))
			.findFirst()
			.orElseThrow(() -> new CommonRuntimeException("Cannot get refund information")).href();
	}
	
	private OrderRequest buildOrderRequest(String returnUrl) {
		return new OrderRequest()
				.checkoutPaymentIntent("CAPTURE")
				.applicationContext(new ApplicationContext().returnUrl(returnUrl));
	}
	
	private String toUsd(String amount) {
		return String.format("%.2f", Long.valueOf(amount).doubleValue() / 22400);
	}
}
