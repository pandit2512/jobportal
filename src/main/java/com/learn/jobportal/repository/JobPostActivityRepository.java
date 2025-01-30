package com.learn.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.jobportal.entity.JobPostActivity;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, Integer> {

}
