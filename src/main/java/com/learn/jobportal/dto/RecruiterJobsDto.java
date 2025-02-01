package com.learn.jobportal.dto;

import com.learn.jobportal.entity.JobCompany;
import com.learn.jobportal.entity.JobLocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterJobsDto {

	private long totalCandidates;
	private Integer jobPostId;
	private String jobTitle;
	private JobLocation jobLocationId;
	private JobCompany jobCompanyId;
}
