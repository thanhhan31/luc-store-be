package com.lucistore.lucistorebe.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.entity.product.ProductVariation;
import com.lucistore.lucistorebe.repo.OrderDetailRepo;
import com.lucistore.lucistorebe.repo.OrderRepo;
import com.lucistore.lucistorebe.repo.ProductCategoryRepo;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.service.PaymentService;
import com.lucistore.lucistorebe.service.ProductCategoryService;
import com.lucistore.lucistorebe.service.ProductService;
import com.lucistore.lucistorebe.service.TransactionService;
import com.lucistore.lucistorebe.service.thirdparty.payment.PayPalService;
import com.lucistore.lucistorebe.service.thirdparty.payment.momo.MomoService;
import com.lucistore.lucistorebe.utility.OtpCache;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;
import com.lucistore.lucistorebe.utility.component.JwtUtil;

@RestController
@RequestMapping("/api")
public class Test {
//	@Autowired
//	JwtUtil jwt;
//	
//	@Autowired
//	ProductCategoryRepo productCategoryRepo;
//	
//	@Autowired
//	ProductCategoryService categoryService;
//	
//	@Autowired
//	OtpCache otpCache;
//	
//	@Autowired
//	OrderRepo orderRepo;
//	
//	/*@PostMapping("/create")
//	public ResponseEntity<?> test(@RequestBody TestRequest request) {
//		return ResponseEntity.ok(categoryService.create(request).getId());
//	}*/
//	
//	@GetMapping("/categoryfinder")
//	private Long categoryfinder(@RequestParam("category") List<String> category) {
//		//List<String> t = Arrays.asList("Máy tính & Laptop", "Phụ Kiện Máy Tính"); //, "Khác"
//		return productCategoryRepo.tmp(category);
//	}
//	
//	/*@Autowired
//	ProductService productService;
//	@PostMapping("/tier-process")
//	public String tier(@RequestBody TestRequest request) {
//		return productService.tier(request) ? "ok" : "no ok";
//	}*/
//	
//	
//	@Autowired
//	PaymentService paymentService;
//	
//	@GetMapping("/otp")
//	public String testpay(HttpServletRequest req) {
//		var t = orderRepo.isBuyerHavePendingOrder(1L);
//		return t ? "hi" : "nohi";
//		//return paymentService.createPayment(2L, 1L, req);
//	}
//	@Autowired
//	OrderDetailRepo orderDetailRepo;
//	
//	@GetMapping("/refund")
//	public String testrefund(HttpServletRequest req) {
//		
//		//paymentService.refundPayment(2L, 1L);
//		return orderDetailRepo.existsByOrderAndReviewed(orderRepo.getReferenceById(1L), false) ? "true" : "false";
//	}
//	
//	/*@Autowired
//	ProductRepo productRepo;
//	@GetMapping("/refresh")
//	public String refresh() {
//		for (var p : productRepo.findAll()) {
//			var pv = new ProductVariation();
//			pv.setProduct(p);
//			productService.updatePriceWhenVariationChange(pv);
//		}
//		
//		/*System.out.println("begin");
//		var x = productRepo.findById(1L).orElseThrow(() -> new CommonRuntimeException("not found"));
//		System.out.println("begin refresh");
//		productRepo.refresh(x);
//		return "a";
//	}*/
//
//	
//	 @GetMapping("/payment-return")
//	 public String testpaypal(@RequestParam("success") Boolean success) {
//		 return String.format("payment-return result: %s", success.toString());
//	 }
//
//	// @GetMapping("/login/oauth2")
//	// public String testoauth(@RequestParam(name = "token") String token) {
//	// 	palService.capture(token);
//	// 	return String.format("Your token is %s", token);
//	// }
}
