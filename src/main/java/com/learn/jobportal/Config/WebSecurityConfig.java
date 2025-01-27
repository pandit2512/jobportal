package com.learn.jobportal.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.learn.jobportal.services.CustomUserDetailsService;

@Configuration
public class WebSecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;
	//creating reference for success handler
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	//Creating Constructor inject for above field
	@Autowired
	public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
		
		this.customUserDetailsService = customUserDetailsService;
	    this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
	}

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
		
		http.authenticationProvider(authenticationProvider());
		//---Step2:---------------------------------------------
				//here we are making USE of publiUrl
				http.authorizeHttpRequests(auth->{
					auth.requestMatchers(publicUrl).permitAll();
				//permitAll():anyone can access without having login
				
				//now if there are anyother request other than assets,resources,register it has to be authenticated.
				//or user has to give userId and password to login
				auth.anyRequest().authenticated();
				
				});
				//creating custom login page & success handler
				http.formLogin(form ->form.loginPage("/login").permitAll().successHandler(customAuthenticationSuccessHandler))
				             .logout(logout->{
				            	 logout.logoutUrl("/logout");
				            	 logout.logoutSuccessUrl("/");
				             }).cors(Customizer.withDefaults())
				               .csrf(csrf->csrf.disable());
				                                     
				
				return http.build();
	}
	//----Step3:--------------------------------------------
      
	
      @Bean
      public AuthenticationProvider authenticationProvider() {
    	  //this is our custome Authentication provider
    	  //it tells Spring Security how to find our users and how to authenticate passwords
    	 //for that we need Dao....
    	  DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
         authenticationProvider.setPasswordEncoder(passwordEncoder());
      
         authenticationProvider.setUserDetailsService(customUserDetailsService);
      // above line tells spring Security how to retrive the users from the database
     
         return authenticationProvider;
      }
      
      @Bean
      public PasswordEncoder passwordEncoder() {
     //here we are setting our custom password encoder
    //which tells Spring Security how to authenticate password (plain text or encryption),will use encryption	  
      
    	  return new BCryptPasswordEncoder();    
     
	}
}
