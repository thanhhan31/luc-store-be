package com.lucistore.lucistorebe.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.entity.product.ProductCategory;
import com.lucistore.lucistorebe.repo.ProductCategoryRepo;
import com.lucistore.lucistorebe.utility.PlatformPolicyParameter;
import com.lucistore.lucistorebe.utility.jwt.JwtUtil;

@RestController
@RequestMapping("/api")
public class Test {
	@Autowired
	JwtUtil jwt;
	
	@Autowired
	ProductCategoryRepo productCategoryRepo;
	
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
}
