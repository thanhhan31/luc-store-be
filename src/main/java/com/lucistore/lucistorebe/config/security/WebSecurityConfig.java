package com.lucistore.lucistorebe.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.lucistore.lucistorebe.config.security.oauth.CustomOAuthUserService;
import com.lucistore.lucistorebe.config.security.oauth.OAuthAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	@Autowired
	CustomOAuthUserService oAuth2UserService;
	
	@Autowired
	OAuthAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	
	@Autowired
	CustomAuthenticationEntryPoint authenticationExceptionHandling;
	
	@Autowired
	CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Bean
    protected BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return authenticationConfiguration.getAuthenticationManager();
    }    
    
    @Bean
    protected AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter();
	}
    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
    		.cors()
    		.and()
	    	.csrf().disable()
	    	.httpBasic().disable()
	    	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		.and()
    		.oauth2Login()
    		.userInfoEndpoint()
    		.userService(oAuth2UserService)
    		.and()
    		.successHandler(oAuth2AuthenticationSuccessHandler)
	    	.and()
	    	.exceptionHandling().authenticationEntryPoint(authenticationExceptionHandling)
	    	.and()
	    	.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
    	
    	http
	    	.authorizeRequests()
			.antMatchers("/api/admin/login",
						"/api/buyer/login", "/api/buyer/signup",
						"/api/buyer/reset-password").permitAll()
			.and()
			.authorizeRequests()
    		.antMatchers("/api/admin/**").hasAnyRole("ADMIN", "SALE_ADMIN")
    		.and()
    		.authorizeRequests()
    		.antMatchers("/api/buyer/**").hasRole("BUYER")
			.and()
    		.authorizeRequests()
    		.antMatchers("/**").permitAll();

    	http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    	return http.build();
    }
    
    @Bean
    protected CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
