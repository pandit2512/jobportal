package com.learn.jobportal.services;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.JobSeekerProfile;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.repository.JobSeekerProfileRepository;
import com.learn.jobportal.repository.UsersRepository;

@Service
public class JobSeekerProfileService {

	private final JobSeekerProfileRepository jobSeekerProfileRepository;
	private final UsersRepository usersRepository;
	
	public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository,UsersRepository usersRepository) {

		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersRepository = usersRepository;
	}


	public Optional<JobSeekerProfile> getOne(Integer id){
		
		return jobSeekerProfileRepository.findById(id);
	}
	
	public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

// used this method in JobSeekerApplyController
	public  JobSeekerProfile getCurrentSeekerProfile() {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			
			String currentUsername = authentication.getName();
		Users users = usersRepository.findByEmail(currentUsername).orElseThrow(()-> 
		    new UsernameNotFoundException("User  not found"));
		
		Optional<JobSeekerProfile> seekerProfile = getOne(users.getUserId());
		return seekerProfile.orElse(null);	
	
		}else return null;
     
  
	}
}
