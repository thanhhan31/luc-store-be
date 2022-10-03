package com.lucistore.lucistorebe.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lucistore.lucistorebe.utility.RandomString;

@Service
public class OtpService {
	LoadingCache<String, String> loader;
	
	@Value("${com.lucistore.lucistorebe.security.otp.expired-after-mins}")
	private long expiredAfterMins;
	
	@Value("${com.lucistore.lucistorebe.security.otp.code-length}")
	private int otpLength;
	
	public OtpService() {
		loader = CacheBuilder
			.newBuilder()
			.expireAfterWrite(expiredAfterMins, TimeUnit.MINUTES)
			.build(new CacheLoader<String, String>() {
				@Override
				public String load(String key) throws Exception {
					return "";
				}
		});
	}
	
	public String create(String username) {
		String otp = RandomString.generateOtp(otpLength);
		loader.put(username, otp);
		return otp;
	}
	
	public String get(String username) {
		try {
			return loader.get(username);
		} catch (ExecutionException e) {
			return "";
		}
	}
	
	public void delete(String username) {
		loader.invalidate(username);
	}
}
