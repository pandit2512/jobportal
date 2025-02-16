package com.learn.jobportal.entity;

import java.beans.Transient;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {
	//---------------------
////Adding a Constructor to take user for jobseeker profile
   public JobSeekerProfile(Users users) {
		this.userId=users;
	}
	//------------------------
	@Id
	private Integer userAccountId;
	
	private String firstName;
	private String lastName;
	private String city;
	private String state;
	private String country;
	private String workAuthorization;
	private String employmentType;
	private String resume;
	
	@Column(nullable = true,length = 64)
	private String profilePhoto;
	
	//---------------------
	@OneToOne
	@JoinColumn(name="user_account_id")
	@MapsId
	private Users userId;
	
	//----------------------
	@OneToMany(targetEntity = Skills.class,cascade =CascadeType.ALL,mappedBy =
			"jobSeekerProfile")
	private List<Skills> skills;

	//-----------Updating Jobseeker profile.
	
	@Transient
	public String getPhotosImagePath() {
		if (profilePhoto==null || userAccountId == null)
			return null;
		return "/photos/candidate/"+userAccountId+ "/"+ profilePhoto;
			
	}
}
