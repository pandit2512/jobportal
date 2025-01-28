package com.learn.jobportal.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.RecruiterProfile;
import com.learn.jobportal.repository.RecruiterProfileRepository;

@Service
public class RecruiterProfileService {

	@Autowired
	private final RecruiterProfileRepository recruiterProfileRepository;

	public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository) {
		this.recruiterProfileRepository = recruiterProfileRepository;
	}
	
	public Optional<RecruiterProfile> getOne(Integer id){
		return recruiterProfileRepository.findById(id);
	}
//Recruiter profile
	public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
		return recruiterProfileRepository.save(recruiterProfile);
		//above code save the recruiter profile
	}
}
