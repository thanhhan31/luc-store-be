package com.lucistore.lucistorebe.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lucistore.lucistorebe.repo.ProductCategoryRepo;
import com.lucistore.lucistorebe.repo.custom.StatisticRepoCustom;
import com.lucistore.lucistorebe.repo.custom.impl.StatisticRepoCustomimpl;
import com.lucistore.lucistorebe.service.TransactionService;
import com.lucistore.lucistorebe.service.thirdparty.PayPalService;
import com.lucistore.lucistorebe.utility.OtpCache;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;
import com.lucistore.lucistorebe.utility.jwt.JwtUtil;

@RestController
@RequestMapping("/api")
public class Test {
	@Autowired
	JwtUtil jwt;
	
	@Autowired
	ProductCategoryRepo productCategoryRepo;
	
	@Autowired
	OtpCache otpCache;
	
	@PreAuthorize("hasAuthority('ALL')")
	@GetMapping
	public String test() {
		return String.format("%d %d %d %d", 
				PlatformPolicyParameter.DEFAULT_PAGE_SIZE,
				PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_IMAGE,
				PlatformPolicyParameter.MAX_ALLOWED_PRODUCT_IMAGE,
				PlatformPolicyParameter.MIN_ALLOWED_PRODUCT_VARIATION);
		//return jwt.parseJwt((HttpServletRequest)null);
		/*List<ProductCategory> l = productCategoryRepo.findAncestry(Long.valueOf(3));
		return l.get(0).getLevel().toString();*/
	}
	
	
	@Autowired
	StatisticRepoCustomimpl statisticRepoCustom;

	
	// @GetMapping("/otp")
	// public String testotp() {
	// 	statisticRepoCustom.statistic(null, null, null, null, 0);
	// 	return "hi";
	// }
	
	@Autowired
	PayPalService palService;
	
	// @GetMapping("/testpaypal")
	// public String testpaypal() {
	// 	return palService.test(1L);
	// }

	// @GetMapping("/login/oauth2")
	// public String testoauth(@RequestParam(name = "token") String token) {
	// 	palService.capture(token);
	// 	return String.format("Your token is %s", token);
	// }
}
