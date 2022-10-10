package com.lucistore.lucistorebe.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.utility.EOtpType;
import com.lucistore.lucistorebe.utility.OtpCache;

@Service
public class OtpService {
	@Autowired
	MailService mailService;
	
	@Autowired
	TwilioService twilioService;
	
	@Autowired
	OtpCache otpCache;
	
	@Autowired
	BuyerService buyerService;

	public void sendOtpViaEmail(String email) {
		Buyer buyer = buyerService.getBuyerByEmail(email);
		if (buyer == null)
			throw new InvalidInputDataException("No user found is associated with this email address");
		
		sendOtpViaEmail(buyer);
	}
	
	public void sendOtpViaEmail(Buyer buyer) {
		sendOtp(buyer, EOtpType.VIA_EMAIL);
	}
	
	public void sendOtpViaPhone(String phoneNumber) {
		Buyer buyer = buyerService.getBuyerByPhoneNumber(phoneNumber);
		if (buyer == null)
			throw new InvalidInputDataException("No user found is associated with this phone number");
		
		sendOtpViaPhone(buyer);
	}
	
	public void sendOtpViaPhone(Buyer buyer) {
		if (StringUtils.isBlank(buyer.getUser().getPhone()))
			throw new InvalidInputDataException("User has no phone number yet");
		sendOtp(buyer, EOtpType.VIA_PHONE);
	}
	
	private void sendOtp(Buyer buyer, EOtpType otpType) {
		String otp = otpCache.create(buyer.getUsername());
		if (otpType == EOtpType.VIA_EMAIL) {
			mailService.sendMailConfirmCode(buyer, otp);
		}
		else if (otpType == EOtpType.VIA_PHONE) {
			twilioService.sendSms(buyer.getUser().getPhone(), otp);
		}
	}
}
