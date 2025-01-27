package com.learn.jobportal.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	//below a list of public URL's, No need for user to login
	//use case of url is that anyone can access(resources,regiter,css etc.) witout having to authenticate
	//anyone can access these assets and resources.
	private final String[] publicUrl = {"/",
			 "/global-search/**",
	            "/register",
	            "/register/**",
	            "/webjars/**",
	            "/resources/**",
	            "/assets/**",
	            "/css/**",
	            "/summernote/**",
	            "/js/**",
	            "/*.css",
	            "/*.js",
	            "/*.js.map",
	            "/fonts**", "/favicon.ico", "/resources/**", "/error"
	       };
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		//here we are making USE of publiUrl
		http.authorizeHttpRequests(auth->{
			auth.requestMatchers(publicUrl).permitAll();
		//permitAll():anyone can access without having login
		
		//now if there are anyother request other than assets,resources,register it has to be authenticated.
		//or user has to give userId and password to login
		auth.anyRequest().authenticated();
		
		});
		
		return http.build();
	}
}
