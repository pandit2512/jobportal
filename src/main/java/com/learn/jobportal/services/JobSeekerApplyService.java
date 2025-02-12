package com.learn.jobportal.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.entity.JobSeekerApply;
import com.learn.jobportal.entity.JobSeekerProfile;
import com.learn.jobportal.repository.JobSeekerApplyRepository;

@Service
public class JobSeekerApplyService {

	@Autowired
	private final JobSeekerApplyRepository jobSeekerApplyRepository;

    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId) {
        return jobSeekerApplyRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity job) {
        return jobSeekerApplyRepository.findByJob(job);
    }
//After Adding above business logic you can use it in controller
    
    //method used in jobseekerapply controller
    public void addNew(JobSeekerApply jobSeekerApply) {
        jobSeekerApplyRepository.save(jobSeekerApply);
    }
}
