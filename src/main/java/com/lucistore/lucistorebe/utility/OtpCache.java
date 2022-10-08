package com.lucistore.lucistorebe.utility;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class OtpCache {
	
	private int otpLength;
	LoadingCache<String, String> loader;
	
	public OtpCache(long expiredAfterMins, int otpLength) {
		loader = CacheBuilder
			.newBuilder()
			.expireAfterWrite(expiredAfterMins, TimeUnit.MINUTES)
			.build(new CacheLoader<String, String>() {
				@Override
				public String load(String key) throws Exception {
					return "not cache";
				}
		});
		this.otpLength = otpLength;
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
			return e.getMessage();
		}
	}
	
	public void delete(String username) {
		loader.invalidate(username);
	}
}
