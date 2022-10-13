package com.lucistore.lucistorebe.service.thirdparty;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.service.PaymentService;
import com.lucistore.lucistorebe.service.TransactionService;
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
@ConfigurationProperties(prefix = "com.lucistore.lucistorebe.service.payment.paypal")
public class PayPalService {
	private String clientId;
	private String clientSecret;
	private PayPalHttpClient payPalHttpClient;
	
	@Autowired
	PaypalOrderIdCache cache;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	PaymentService paymentService;

	@PostConstruct
    public void init() {
        payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Sandbox(clientId, clientSecret));
    }
    
	public String createOrder(Long idOrder, String amount, String returnUrl) {
		OrderRequest orderRequest = buildOrderRequest(returnUrl); //"http://localhost:8080/api/login/oauth2"
		
		PurchaseUnitRequest purchaseUnitRequest = 
			new PurchaseUnitRequest()
	            .amountWithBreakdown(
            		new AmountWithBreakdown()
            		.currencyCode("USD")
            		.value(amount)
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
	
	public void captureOrder(String orderId) {
		OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(orderId);
		try {
			HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
			if (httpResponse.result().status().equals("COMPLETED")) {
				Long idOrder = cache.get(orderId);
				String captureId = httpResponse.result().purchaseUnits().get(0).payments().captures().get(0).id();
				if (paymentService.confirm(idOrder)) {
					transactionService.assignTransaction(idOrder, captureId);
				}
				else {
					refundCapture(captureId);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refundCapture(Long idOrder) {
		refundCapture(transactionService.getByIdOrder(idOrder).getData());
		transactionService.setRefund(idOrder);
	}
	
	public void refundCapture(String captureId) {
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
}
