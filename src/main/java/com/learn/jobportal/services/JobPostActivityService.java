package com.learn.jobportal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.repository.JobPostActivityRepository;

@Service
public class JobPostActivityService {

	@Autowired
	private final JobPostActivityRepository jobPostActivityRepository;
	
	public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
		this.jobPostActivityRepository=jobPostActivityRepository;
		
	}
	
	
	public JobPostActivity addNew(JobPostActivity jobPostActivity) {
		//saving to Repo
		return jobPostActivityRepository.save(jobPostActivity);
	}
}
