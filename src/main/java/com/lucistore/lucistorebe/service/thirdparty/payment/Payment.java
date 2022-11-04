package com.lucistore.lucistorebe.service.thirdparty.payment;

import javax.servlet.http.HttpServletRequest;

public interface Payment {
	
	String createPayment(Long idOrder, String amount, HttpServletRequest req);
	
	void refundPayment(Long idOrder);
	
}
