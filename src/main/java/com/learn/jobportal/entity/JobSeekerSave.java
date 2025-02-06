package com.learn.jobportal.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"userId", "job"})
	})
public class JobSeekerSave implements Serializable {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "userId", referencedColumnName = "user_account_id")
	    private JobSeekerProfile userId;

	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "job", referencedColumnName = "jobPostId")
	    private JobPostActivity job;

}
