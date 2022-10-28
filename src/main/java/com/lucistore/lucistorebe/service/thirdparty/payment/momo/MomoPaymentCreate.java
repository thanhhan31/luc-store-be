package com.lucistore.lucistorebe.service.thirdparty.momo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MomoPaymentCreate {
	@JsonIgnore
	private String secretKey;
	
	private String partnerCode;
	private static final String requestType = "captureWallet";
	private static final String extraData = "";
	private static final boolean autoCapture = false;
	private String ipnUrl;
	private String redirectUrl;
	private String orderId;
	private String amount;
	private String orderInfo;
	private String requestId;
	private String signature;
	
	public MomoPaymentCreate(String ipnUrl, String redirectUrl, String orderId, String amount, String orderInfo,
			String requestId, String secretKey, String accessKey, String partnerCode) {
		this.secretKey = secretKey;
		this.partnerCode = partnerCode;
		this.ipnUrl = ipnUrl;
		this.redirectUrl = redirectUrl;
		this.orderId = orderId;
		this.amount = amount;
		this.orderInfo = orderInfo;
		this.requestId = requestId;
		
		String concat = "accessKey=" + accessKey +
				"&amount=" + amount +
				"&extraData=" + 
				"&ipnUrl=" + ipnUrl + 
				"&orderId=" + orderId +
				"&orderInfo=" + orderInfo +
				"&partnerCode=" + partnerCode +
				"&redirectUrl=" + redirectUrl +
				"&requestId=" + requestId +
				"&requestType=" + requestType;
		
		try {
			signature =  DataSigning.sign(secretKey, concat);
		} catch (Exception e) {
			throw new CommonRuntimeException("Error when signing data for Momo payment");
		}
	}
}
