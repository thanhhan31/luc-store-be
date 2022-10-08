package com.lucistore.lucistorebe.config.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.lucistore.lucistorebe.service.BuyerService;
import com.lucistore.lucistorebe.service.auth.LoginService;

@Component
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final BuyerService buyerService;
	
	private final LoginService loginService;
	
	@Value("${com.lucistore.lucistorebe.security.oauth2.fe-login-url}")
	String redirectUrl;
	
	public OAuthAuthenticationSuccessHandler(@Lazy BuyerService buyerService, @Lazy LoginService loginService) {
		this.buyerService = buyerService;
		this.loginService = loginService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomOAuthUser user = (CustomOAuthUser)authentication.getPrincipal();
		String email = user.getEmail();
		String username;
		
		if (!buyerService.existsByEmail(email))
			username = buyerService.createOAuth2User(email, user.getName());
		else
			username = buyerService.getUsernameByEmail(email);
		
		getRedirectStrategy().sendRedirect(
				request, 
				response, 
				redirectUrl + "?token=" + loginService.generateToken(username));
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
