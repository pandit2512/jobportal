package com.learn.jobportal.Controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.services.JobPostActivityService;
import com.learn.jobportal.services.UsersService;

@Controller
public class JobPostActivityController {
    
	@Autowired
	private final UsersService usersService;
//----------------
	private final JobPostActivityService jobPostActivityService;
	
	public JobPostActivityController(UsersService usersService,JobPostActivityService jobPostActivityService) {
		
		this.usersService = usersService;
		this.jobPostActivityService=jobPostActivityService;
	}

	@GetMapping("/dashboard/")
	public String searchJobs(Model model) {
		
		Object currentUserProfile = usersService.getCurrentUserProfile();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
		  
			String currentUsername=authentication.getName();
			model.addAttribute("username",currentUsername);		
		}
		
		model.addAttribute("user",currentUserProfile);
		return "dashboard";		
	}
	
	//------sec-6-Adding to enhance for posting a new in job JobPostActivity Controller
    @GetMapping("/dashboard/add")
	public String addJobs(Model model) {
    	 model.addAttribute("jobPostActivity", new JobPostActivity());
    	 model.addAttribute("user", usersService.getCurrentUserProfile());
    	 
    	 return "add-jobs";
     }
	
    //------V-30- recruiter post job--
   @PostMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity,Model model) {
    	Users user = usersService.getCurrentUser();
	   if(user != null) {
		   jobPostActivity.setPostedById(user);
	   }
	   jobPostActivity.setPostedDate(new Date());
	   model.addAttribute("jobPostActivity",jobPostActivity);
	   
	   //saving job post activity to the dataBase
	   JobPostActivity saved = jobPostActivityService.addNew(jobPostActivity);
	   return "redirect:/dashboard/";
	   
    
   }
}
