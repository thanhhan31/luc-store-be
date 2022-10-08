package com.lucistore.lucistorebe.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.utility.OtpCache;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class MailService {
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	OtpCache otpCache;
	
	@Autowired
	BuyerService buyerService;
		
	@Value("${spring.mail.username}")
	private String senderMail;
	
	@Value("${com.lucistore.lucistorebe.security.otp.expired-after-mins}")
	private long expiredAfterMins;
	
	@Autowired
	private Configuration freeMakerConfiguration;
	
	public void sendMailConfirmCode(String email) {
		Buyer buyer = buyerService.getBuyerByEmail(email);
		if (buyer == null)
			throw new InvalidInputDataException("No user found is associated with this email address");
		sendMailConfirmCode(buyer);
	}
	
	public void sendMailConfirmCode(Buyer buyer) {
		/*if (buyer.getEmailConfirmed().booleanValue())
			throw new CommonRestException("Email has been already confirmed");*/
		String otp = otpCache.create(buyer.getUsername());
		
		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			
			Map<String, Object> model = new HashMap<>();
			model.put("fullname", buyer.getUser().getFullname());
			model.put("otp", otp);
			model.put("expiredAfterMins", expiredAfterMins);
			
			Template t = freeMakerConfiguration.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			helper.setTo(buyer.getUser().getEmail());
			helper.setText(html, true);
			helper.setSubject("[LUC Store] Email confirmation code");
			helper.setFrom(senderMail);
			
			mailSender.send(message);
			
		} catch (Exception e) {
			throw new CommonRuntimeException("Failed to send mail confirmation code (" + e.getMessage() + ")");
		}
	}
}
