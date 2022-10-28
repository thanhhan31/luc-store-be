package com.lucistore.lucistorebe.service.thirdparty.momo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.service.TransactionService;
import com.lucistore.lucistorebe.utility.ExecuteRequest;
import com.lucistore.lucistorebe.utility.RandomString;

@Service
public class MomoService {
	@Autowired
	Environment environment;
	
	@Autowired
	TransactionService transactionService;
	
	@Value("${com.lucistore.lucistorebe.service.payment.momo.secret-key}")
	private String secretKey;
	
	@Value("${com.lucistore.lucistorebe.service.payment.momo.access-key}")
	private String accessKey;
	
	@Value("${com.lucistore.lucistorebe.service.payment.momo.partner-code}")
	private String partnerCode;
	
	@Value("${com.lucistore.lucistorebe.service.payment.momo.url.payment-create}")
	private String paymentCreate;
	
	@Value("${com.lucistore.lucistorebe.service.payment.momo.url.payment-confirm}")
	private String paymentConfirm;
	
	public String getOrderPayUrl(Long idOrder, String notifyUrl, String returnUrl, String amount) {
		String encodedIdOrder = String.format("%d-%s", idOrder, RandomString.get(6));
		return getPayUrl(encodedIdOrder, notifyUrl, returnUrl, amount);
	}
	
	public boolean confirmPayment(String orderId, String amount, String transId, Long idOrder, boolean isConfirm) {
		String resp = "";
		try {
			resp = ExecuteRequest.execute(
					paymentConfirm, 
					new MomoPaymentConfirm(
							accessKey,
							secretKey,
							partnerCode,
							UUID.randomUUID().toString(),
							orderId,
							amount,
							isConfirm)
					);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			System.err.println(resp);
			Map<String, Object> m = new ObjectMapper().readValue(resp, HashMap.class);
			if ((int)m.get("resultCode") == 0) {
				if (isConfirm)
					transactionService.assignTransaction(idOrder, transId);
				return true;
			}
			else {
				System.err.println(String.format("Confirm Momo bill failed (%s)", resp));
			}
		} catch (Exception e) {
			System.err.println(String.format("Confirm Momo bill failed (%s)", resp));
		}
		return false;
	}
	
	private String getPayUrl(String idOrder, String ipnUrl, String redirectUrl, String amount) {
		String resp = "";
		try {
			resp = ExecuteRequest.execute(
					paymentCreate, 
					new MomoPaymentCreate(
							ipnUrl,
							redirectUrl, 
							idOrder,
							amount, 
							"TTFSOFT-Shopee", 
							UUID.randomUUID().toString(),
							secretKey,
							accessKey,
							partnerCode)
					);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		try {
			System.err.println(resp);
			Map<String, Object> m = new ObjectMapper().readValue(resp, HashMap.class);
			if ((int)m.get("resultCode") == 0) {
				return (String)m.get("payUrl");
			}
			else {
				throw new CommonRuntimeException(String.format("Generate Momo bill failed (%s)", resp));
			}
		} catch (JsonProcessingException  e) {
			throw new CommonRuntimeException(e.getMessage());
		}
	}
}
