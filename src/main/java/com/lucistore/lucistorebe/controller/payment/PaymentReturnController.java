package com.lucistore.lucistorebe.controller.payment;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lucistore.lucistorebe.service.PaymentService;
import com.lucistore.lucistorebe.service.thirdparty.payment.PayPalService;

@Controller
@RequestMapping("/api/payment/return")
public class PaymentReturnController {
	@Autowired 
	PayPalService payPalService;
	
	@Autowired
	PaymentService paymentService;
	
	@GetMapping("/momo")
	public ResponseEntity<Void> momoReturn(@RequestParam("resultCode") Integer resultCode) {
		return createRedirect(resultCode.equals(9000));
	}
	
	@GetMapping("/paypal")
	public ResponseEntity<Void> paypalReturn(@RequestParam(name = "token") String token) {
		return createRedirect(payPalService.captureOrder(token));
	}
	
	private ResponseEntity<Void> createRedirect(boolean success) {
		return ResponseEntity.status(HttpStatus.FOUND)
		        .location(URI.create(paymentService.getReturnUrl(success)))
		        .build();
	}
}
