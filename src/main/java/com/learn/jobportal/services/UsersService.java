package com.learn.jobportal.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.JobSeekerProfile;
import com.learn.jobportal.entity.RecruiterProfile;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.repository.JobSeekerProfileRepository;
import com.learn.jobportal.repository.RecruiterProfileRepository;
import com.learn.jobportal.repository.UsersRepository;

@Service
public class UsersService {

	private final UsersRepository usersRepository;
	//----------------------
	private final JobSeekerProfileRepository jobSeekerProfileRepository;
	private final RecruiterProfileRepository recruiterProfileRepository;

	//now inject these in constructor
	//------------------------
	public UsersService(UsersRepository usersRepository,
			     JobSeekerProfileRepository jobSeekerProfileRepository,
			         RecruiterProfileRepository recruiterProfileRepository) {
		
		this.usersRepository=usersRepository;
		this.jobSeekerProfileRepository=jobSeekerProfileRepository;
		this.recruiterProfileRepository=recruiterProfileRepository;
		
	}

	public Users addNew(Users users) {
		users.setActive(true);
		
		users.setRegistrationDate(new Date(System.currentTimeMillis()));
		Users savedUser = usersRepository.save(users);
		//---------------------------
		int userTypeId = users.getUserTypeId().getUserTypeId();
		if(userTypeId==1) {
			//if usertype = 1 then save this person as recruiter
			recruiterProfileRepository.save(new RecruiterProfile(savedUser));
		}else {
			jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
		}
		
		//-------------------
		return savedUser;
	}
	
	public Optional<Users> getUserByEmail(String email){
		
		return usersRepository.findByEmail(email);
	}
}
