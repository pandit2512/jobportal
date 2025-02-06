package com.learn.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.entity.JobSeekerApply;
import com.learn.jobportal.entity.JobSeekerProfile;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {

	//Adding Supporting method to findUserById and findByJob
	 List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

	    List<JobSeekerApply> findByJob(JobPostActivity job);
}
