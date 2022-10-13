package com.lucistore.lucistorebe.utility;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class PaypalOrderIdCache {
	LoadingCache<String, Long> loader;
	
	public PaypalOrderIdCache() {
		loader = CacheBuilder
			.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build(new CacheLoader<String, Long>() {
				@Override
				public Long load(String key) throws Exception {
					return -1L;
				}
		});
	}
	
	public void create(String orderId, Long idOrder) {
		loader.put(orderId, idOrder);
	}
	
	public Long get(String orderId) {
		try {
			return loader.get(orderId);
		} catch (ExecutionException e) {
			return -1L;
		}
	}
}
