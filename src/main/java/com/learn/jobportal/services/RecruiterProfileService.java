package com.learn.jobportal.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.RecruiterProfile;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.repository.RecruiterProfileRepository;
import com.learn.jobportal.repository.UsersRepository;

@Service
public class RecruiterProfileService {

	@Autowired
	private final RecruiterProfileRepository recruiterRepository;
	@Autowired
	private final UsersRepository usersRepository;

    
	public RecruiterProfileService(RecruiterProfileRepository recruiterRepository, UsersRepository usersRepository) {
		super();
		this.recruiterRepository = recruiterRepository;
		this.usersRepository = usersRepository;
	}
	
	public Optional<RecruiterProfile> getOne(Integer id){
		return recruiterRepository.findById(id);
	}
//Recruiter profile
	public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
		return recruiterRepository.save(recruiterProfile);
		//above code save the recruiter profile
	}

	//----------Used this method in JobSeekerApplyController
	public static RecruiterProfile getCurrentRecruiterProfile() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Optional<RecruiterProfile> recruiterProfile = getOne(users.getUserId());
            return recruiterProfile.orElse(null);
        } else return null;

	}
}
