package com.learn.jobportal.Controller;

import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.services.JobPostActivityService;
import com.learn.jobportal.services.UsersService;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    
    private final UsersService usersService;

    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
    }

    // method to display a job
    @GetMapping("job-details-apply/{id}") //this path is there in jobdetails.html
    public String display(@PathVariable("id") int id, Model model) {
        JobPostActivity jobDetails = jobPostActivityService.getOne(id);

        model.addAttribute("jobDetails", jobDetails);
       // adding users info to model
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }
}
