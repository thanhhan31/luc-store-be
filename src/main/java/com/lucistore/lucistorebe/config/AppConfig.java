package com.lucistore.lucistorebe.config;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.lucistore.lucistorebe.utility.OtpCache;


@Configuration
public class AppConfig {
	@Value("${com.lucistore.lucistorebe.security.otp.expired-after-mins}")
	private long expiredAfterMins;
	
	@Value("${com.lucistore.lucistorebe.security.otp.code-length}")
	private int otpLength;
	
	@Bean
	public OtpCache otpCache() {
		return new OtpCache(expiredAfterMins, otpLength);
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter octetStreamJsonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(
				Arrays.asList(
						MediaType.APPLICATION_JSON, 
						MediaType.APPLICATION_OCTET_STREAM));
		converter.getObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return converter;
	}
}
