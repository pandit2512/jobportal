package com.learn.jobportal.Controller;

import org.springframework.stereotype.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.entity.JobSeekerApply;
import com.learn.jobportal.entity.JobSeekerProfile;
import com.learn.jobportal.entity.JobSeekerSave;
import com.learn.jobportal.entity.RecruiterProfile;
import com.learn.jobportal.services.JobPostActivityService;
import com.learn.jobportal.services.JobSeekerApplyService;
import com.learn.jobportal.services.JobSeekerProfileService;
import com.learn.jobportal.services.JobSeekerSaveService;
import com.learn.jobportal.services.RecruiterProfileService;
import com.learn.jobportal.services.UsersService;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    
    private final UsersService usersService;
//--------During adding code for "Applying" a job
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;
    
    @Autowired
    public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService,
    		JobSeekerApplyService jobSeekerApplyService,
    		JobSeekerSaveService jobSeekerSaveSerice,
    		RecruiterProfileService recruiterProfileService, 
    		JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveSerice;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    // method to display a job
    @GetMapping("job-details-apply/{id}") //this path is there in jobdetails.html
    public String display(@PathVariable("id") int id, Model model) {
        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        
        //-------candidate apply for job
        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getJobCandidates(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getJobCandidates(jobDetails);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile user = recruiterProfileService.getCurrentRecruiterProfile();
                if (user != null) {
                    model.addAttribute("applyList", jobSeekerApplyList);
                }
            } else {
                JobSeekerProfile user = jobSeekerProfileService.getCurrentSeekerProfile();
                if (user != null) {
                    boolean exists = false;
                    boolean saved = false;
                    for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                        if (jobSeekerApply.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            exists = true;
                            break;
                        }
                    }
                    for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
                        if (jobSeekerSave.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            saved = true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied", exists);
                    model.addAttribute("alreadySaved", saved);
                }
            }
        }

        JobSeekerApply jobSeekerApply = new JobSeekerApply();
        model.addAttribute("applyJob", jobSeekerApply);
        
        model.addAttribute("jobDetails", jobDetails);
       // adding users info to model
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }
}
