package com.lucistore.lucistorebe.utility.component;

import java.util.Arrays;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExecuteExternalRequest {
	private RestTemplate restTemplate = null; 
	
	public ExecuteExternalRequest() { 
		restTemplate = new RestTemplateBuilder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		restTemplate.setMessageConverters(Arrays.asList(new StringHttpMessageConverter(), new MappingJackson2HttpMessageConverter()));
	}
	
	public String postJson(String url, Object payload) {
		return restTemplate.postForEntity(url, payload, String.class).getBody();
	}
}
