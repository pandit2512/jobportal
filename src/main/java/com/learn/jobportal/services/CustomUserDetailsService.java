package com.learn.jobportal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.Users;
import com.learn.jobportal.repository.UsersRepository;
import com.learn.jobportal.util.CustomUserDetails;

//This Service tells Spring Security how to retrive the users from the database
@Service
public class CustomUserDetailsService implements UserDetailsService {

	//1.Adding reference to userRepository that we have created
	@Autowired
	private final UsersRepository usersRepository;
	
	//2.Adding Constructor injection for above
	public CustomUserDetailsService(UsersRepository usersRepository) {
		
		this.usersRepository = usersRepository;
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could not found user"));
		return new CustomUserDetails(user);
	}

	

}
