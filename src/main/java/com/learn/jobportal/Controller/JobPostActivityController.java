package com.learn.jobportal.Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learn.jobportal.dto.RecruiterJobsDto;
import com.learn.jobportal.entity.JobPostActivity;
import com.learn.jobportal.entity.JobSeekerApply;
import com.learn.jobportal.entity.JobSeekerProfile;
import com.learn.jobportal.entity.JobSeekerSave;
import com.learn.jobportal.entity.RecruiterProfile;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.services.JobPostActivityService;
import com.learn.jobportal.services.JobSeekerApplyService;
import com.learn.jobportal.services.JobSeekerSaveService;
import com.learn.jobportal.services.UsersService;

@Controller
public class JobPostActivityController {
    
	@Autowired
	private final UsersService usersService;
//----------------
	private final JobPostActivityService jobPostActivityService;
//---during recruiter dashboard
	private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

	@GetMapping("/dashboard/")
	public String searchJobs(Model model,
			                    @RequestParam(value = "job",required = false)String job ,
			                    @RequestParam(value = "location",required = false) String location,
                                @RequestParam(value = "partTime",required = false) String partTime, 
		                        @RequestParam(value = "fullTime",required = false) String fullTime,
	                            @RequestParam(value = "freelance", required = false) String freelance,
                                @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                                @RequestParam(value = "officeOnly", required = false) String officeOnly,
                                @RequestParam(value = "partialRemote", required = false) String partialRemote,
                                @RequestParam(value = "today", required = false) boolean today,
                                @RequestParam(value = "days7", required = false) boolean days7,
                                @RequestParam(value = "days30", required = false) boolean days30
                        ){
		//JobCandidate dashboard ---- Adding to the model
		model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(partTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(partTime, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(partTime, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(partTime, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partTime, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;
     // for option like last 7 days and 30days
        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }
        
		//now setting default value if in case these value did't comes

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }
     //Adding code to check search flag
        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = jobPostActivityService.getAll();
        } else {
            jobPost = jobPostActivityService.search(job, location, Arrays.asList(partTime, fullTime, freelance),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }
		//---------------------------------
		Object currentUserProfile = usersService.getCurrentUserProfile();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
		  
			String currentUsername=authentication.getName();
			model.addAttribute("username",currentUsername);		
		//-----during recruiter dashboard
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
				
				List<RecruiterJobsDto> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile)
						                                     .getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);			}
		}else {
            List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
            List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);

            boolean exist;
            boolean saved;

            for (JobPostActivity jobActivity : jobPost) {
                exist = false;
                saved = false;
                for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                    if (Objects.equals(jobActivity.getJobPostId(), jobSeekerApply.getJob().getJobPostId())) {
                        jobActivity.setIsActive(true);
                        exist = true;
                        break;
                    }
                }

                for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
                    if (Objects.equals(jobActivity.getJobPostId(), jobSeekerSave.getJob().getJobPostId())) {
                        jobActivity.setIsSaved(true);
                        saved = true;
                        break;
                    }
                }

                if (!exist) {
                    jobActivity.setIsActive(false);
                }
                if (!saved) {
                    jobActivity.setIsSaved(false);
                }
                // now adding jobPost to model
                model.addAttribute("jobPost", jobPost);

            }
        }
    
		
		model.addAttribute("user",currentUserProfile);
		return "dashboard";		
}
	//---sec:11: Global Search----
	//the code would some what similar to above
	@GetMapping("global-search/")
    public String globalSearch(Model model,
                               @RequestParam(value = "job", required = false) String job,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "partTime", required = false) String partTime,
                               @RequestParam(value = "fullTime", required = false) String fullTime,
                               @RequestParam(value = "freelance", required = false) String freelance,
                               @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                               @RequestParam(value = "officeOnly", required = false) String officeOnly,
                               @RequestParam(value = "partialRemote", required = false) String partialRemote,
                               @RequestParam(value = "today", required = false) boolean today,
                               @RequestParam(value = "days7", required = false) boolean days7,
                               @RequestParam(value = "days30", required = false) boolean days30) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(partTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(partTime, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(partTime, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(partTime, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partTime, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }

        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = jobPostActivityService.getAll();
        } else {
            jobPost = jobPostActivityService.search(job, location, Arrays.asList(partTime, fullTime, freelance),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }

        model.addAttribute("jobPost", jobPost);
        return "global-search";
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
   //We need to add a request mapping for: /edit/{id}
   @PostMapping("dashboard/edit/{id}")
   public String editJob(@PathVariable("id") int id,Model model) {
	   
	   JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
	  model.addAttribute("jobPostActivity",jobPostActivity);
	  model.addAttribute("user", usersService.getCurrentUserProfile());
	   return "add-jobs";
	   
   }
}
