package com.learn.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.jobportal.entity.JobSeekerProfile;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Integer> {

}
