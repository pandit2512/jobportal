package com.learn.jobportal.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.jobportal.dto.RecruiterJobsDto;
import com.learn.jobportal.entity.JobCompany;
import com.learn.jobportal.entity.JobLocation;
import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.entity.RecruiterJobs;
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
	//during recruiter dashboard
	
	public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
		
		List<RecruiterJobs> recruiterJobsDtos = jobPostActivityRepository.getRecruiterJobs(recruiter);
		
		List<RecruiterJobsDto> recruiterJobsDtoList = new ArrayList<>();
		
		//convert info from database to Dto's
		for(RecruiterJobs rec : recruiterJobsDtos) {
			//Construct DTO based on info from database
			JobLocation location = new JobLocation(rec.getLocationId(), rec.getCity(),rec.getState(),rec.getCountry());
			JobCompany company = new JobCompany(rec.getCompanyId(),rec.getName(),"");
		     
			recruiterJobsDtoList.add(new RecruiterJobsDto(rec.getTotalCandidates(),rec.getJobPostId(),
				       rec.getJobTitle(), location,company));		
		}
		
		return recruiterJobsDtoList;
			
	}

	public JobPostActivity getOne(int id) {
		// TODO Auto-generated method stub
		return jobPostActivityRepository.findById(id).orElseThrow(()-> new RuntimeException("Job not found"));
	}

//------------------------------------------	
//used these method in JobPostActivitycontroller(during recruiter dashboard)
	public List<JobPostActivity> getAll() {
		
		return jobPostActivityRepository.findAll();
	}

	public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote,
			LocalDate searchDate) {
		return Objects.isNull(searchDate)?jobPostActivityRepository.searchWithoutDate(job,location,remote,type):
			   jobPostActivityRepository.search(job, location, remote, type, searchDate);
	
	}
	
}
