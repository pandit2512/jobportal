package com.learn.jobportal.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.entity.JobSeekerProfile;
import com.learn.jobportal.entity.JobSeekerSave;
import com.learn.jobportal.repository.JobSeekerSaveRepository;

@Service
public class JobSeekerSaveService {

	 private final JobSeekerSaveRepository jobSeekerSaveRepository;

	    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
	        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
	    }

	    public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId) {
	        return jobSeekerSaveRepository.findByUserId(userAccountId);
	    }

	    public List<JobSeekerSave> getJobCandidates(JobPostActivity job) {
	        return jobSeekerSaveRepository.findByJob(job);
	    }
}
