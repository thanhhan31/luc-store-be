package com.lucistore.lucistorebe.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Service
@ConfigurationProperties(prefix = "com.lucistore.lucistorebe.service.twilio")
public class TwilioService {
	private String number;
	private String accountSid;
	private String authToken;
	
	@Value("${com.lucistore.lucistorebe.security.otp.expired-after-mins}")
	private long expiredAfterMins;

    @PostConstruct
    private void init() {
    	Twilio.init(accountSid, authToken);
    }
    
    public void sendSms(String phoneNumber, String otpCode) {
    	Message.creator(
			new PhoneNumber(phoneNumber), 
			new PhoneNumber(number), 
			"Your OTP code is: " + otpCode)
    	.create();
    }
}
