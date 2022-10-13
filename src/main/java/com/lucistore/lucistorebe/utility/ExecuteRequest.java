package com.lucistore.lucistorebe.utility;

import org.springframework.web.client.RestTemplate;

public final class ExecuteRequest {
	public static String execute(String url, Object payload) {
		return new RestTemplate().postForEntity(url, payload, String.class).getBody();
	}
	
	private ExecuteRequest() { }
}
