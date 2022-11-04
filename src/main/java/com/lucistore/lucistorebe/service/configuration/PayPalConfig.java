package com.lucistore.lucistorebe.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "com.lucistore.lucistorebe.service.payment.paypal")
public class PayPalConfig {
	private String clientId;
	private String clientSecret;
	private Url url;
	
	@Getter @Setter
	@NoArgsConstructor
	public static class Url {
		private String callback;
	}
}
