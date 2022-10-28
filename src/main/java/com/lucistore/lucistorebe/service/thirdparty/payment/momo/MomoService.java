package com.lucistore.lucistorebe.service.thirdparty.payment.momo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.service.TransactionService;
import com.lucistore.lucistorebe.service.configuration.MomoConfig;
import com.lucistore.lucistorebe.service.thirdparty.payment.Payment;
import com.lucistore.lucistorebe.service.thirdparty.payment.Utils;
import com.lucistore.lucistorebe.utility.RandomString;
import com.lucistore.lucistorebe.utility.component.ExecuteExternalRequest;

@Service
public class MomoService implements Payment {
	@Autowired
	Environment environment;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	ExecuteExternalRequest executeExternalRequest;
	
	@Autowired
	MomoConfig momoConfig;
	
	@Override
	public String createPayment(Long idOrder, String amount, HttpServletRequest req) {
		return createPayment(
				String.format("%d-%s", idOrder, RandomString.get(6)), 
				Utils.buildUrl(momoConfig.getUrl().getNotify(), req), 
				Utils.buildUrl(momoConfig.getUrl().getCallback(), req),
				amount
			);
	}
	
	@Override
	public void refundPayment(Long idOrder) {
		var transaction = transactionService.getByIdOrder(idOrder);
		String transId = transaction.getData();
		
		String resp = "";
		try {
			resp = executeExternalRequest.postJson(
					momoConfig.getUrl().getPaymentRefund(), 
					new MomoPaymentRefund(
							momoConfig.getAccessKey(),
							momoConfig.getSecretKey(),
							momoConfig.getPartnerCode(),
							UUID.randomUUID().toString(),
							UUID.randomUUID().toString(),
							transId,
							transaction.getOrder().getPayPrice().toString())
					);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonRuntimeException("Failed to call Momo refund API");
		}
		
		try {
			Map<String, Object> m = new ObjectMapper().readValue(resp, HashMap.class);
			if ((int)m.get("resultCode") == 0) 
				transactionService.setRefund(idOrder);
			else
				throw new CommonRuntimeException(String.format("Momo refund failed (%s)", resp));
		} catch (Exception e) {
			throw new CommonRuntimeException(String.format("Failed to parse response from Momo refund API (%s)", resp));
		}
	}
	
	public boolean confirmPayment(String orderId, String amount, String transId, Long idOrder, boolean isConfirm) {
		String resp = "";
		try {
			resp = executeExternalRequest.postJson(
					momoConfig.getUrl().getPaymentConfirm(), 
					new MomoPaymentConfirm(
							momoConfig.getAccessKey(),
							momoConfig.getSecretKey(),
							momoConfig.getPartnerCode(),
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
			Map<String, Object> m = new ObjectMapper().readValue(resp, HashMap.class);
			if ((int)m.get("resultCode") == 0) {
				if (isConfirm)
					transactionService.assignTransaction(idOrder, transId);
				return true;
			}
		} catch (Exception e) { }
		
		return false;
	}
	
	private String createPayment(String idOrder, String ipnUrl, String redirectUrl, String amount) {
		String resp = "";
		try {
			resp = executeExternalRequest.postJson(
					momoConfig.getUrl().getPaymentCreate(), 
					new MomoPaymentCreate(
							ipnUrl,
							redirectUrl, 
							idOrder,
							amount, 
							momoConfig.getStoreName(), 
							UUID.randomUUID().toString(),
							momoConfig.getSecretKey(),
							momoConfig.getAccessKey(),
							momoConfig.getPartnerCode())
					);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonRuntimeException("Failed to call Momo create API");
		}
		
		try {
			Map<String, Object> m = new ObjectMapper().readValue(resp, HashMap.class);
			if ((int)m.get("resultCode") == 0) {
				return (String)m.get("payUrl");
			}
			else {
				throw new CommonRuntimeException(String.format("Generate Momo bill failed (%s)", resp));
			}
		} catch (JsonProcessingException  e) {
			throw new CommonRuntimeException(String.format("Generate Momo bill failed (%s)", e.getMessage()));
		}
	}
}
