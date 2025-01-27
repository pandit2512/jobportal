package com.learn.jobportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {
//-------------------------------	
//Adding a Constructor to take user for recruiter profile
	public RecruiterProfile(Users users) {
	   this.userId=users;
	}
//----------------------------------------
	@Id
	private int userAccountId;
	
	private String firstName;
	private String lastName;
	private String city;
	private String state;
	private String country;
	private String company;
	
	@Column(nullable = true,length=64)
	private String profilePhoto;
	
	@OneToOne
	@JoinColumn(name = "user_account_id")
	@MapsId
	private Users userId;
	
}
