package com.learn.jobportal.Controller;

import com.learn.jobportal.entity.JobSeekerProfile;
import com.learn.jobportal.entity.Skills;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.repository.UsersRepository;
import com.learn.jobportal.services.JobSeekerProfileService;
import com.learn.jobportal.util.FileDownloadUtil;
//import com.learn.jobportal.util.FileDownloadUtil;
import com.learn.jobportal.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")   // adding base path for Jobseeker
public class JobSeekerProfileController {

	private JobSeekerProfileService jobSeekerProfileService;
	
	private UsersRepository usersRepository;

	public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService,
			              UsersRepository usersRepository) {

		this.jobSeekerProfileService = jobSeekerProfileService;
	    this.usersRepository = usersRepository;
	}
	
	@GetMapping("/")
	public String JobSeekerProfile(Model model) {


		//--5min
		JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Skills> skills = new ArrayList<Skills>();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			
			Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(()
					-> new UsernameNotFoundException("User not found"));
			
			Optional<JobSeekerProfile> seekerProfile =jobSeekerProfileService.getOne(user.getUserId());
			
			if(seekerProfile.isPresent()) {
				jobSeekerProfile=seekerProfile.get();
				if(jobSeekerProfile.getSkills().isEmpty()) {
					skills.add(new Skills());
					jobSeekerProfile.setSkills(skills);
				}
			}
			//now adding Above info to Model
			model.addAttribute("skills",skills);
			model.addAttribute("profile",jobSeekerProfile);
		}
		
		return "job-seeker-profile";
		
	}
//---------------------
	
	@PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(()
            		     -> new UsernameNotFoundException("User not found."));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        List<Skills> skillsList = new ArrayList<>();
        //Adding above data in model
        model.addAttribute("profile", jobSeekerProfile);
        model.addAttribute("skills", skillsList);

        for (Skills skills : jobSeekerProfile.getSkills()) {
            skills.setJobSeekerProfile(jobSeekerProfile);
        }

        String imageName = "";
        String resumeName = "";
        //1st section for profilr image
        if (!Objects.equals(image.getOriginalFilename(), "")) {
            imageName = org.springframework.util.StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }
       //2nd Section for Resume
        if (!Objects.equals(pdf.getOriginalFilename(), "")) {
            resumeName = org.springframework.util.StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }
     // Now we need to save above info to our dataBase using JobProfileService
        JobSeekerProfile seekerProfile = jobSeekerProfileService.addNew(jobSeekerProfile);
        
        //we need to add some code to save the file to the file System
        try {
            String uploadDir = "photos/candidate/" + jobSeekerProfile.getUserAccountId();
            if (!Objects.equals(image.getOriginalFilename(), "")) {
                //below code,to save file the for the profile image
            	FileUploadUtil.saveFile(uploadDir, imageName, image);
            }
            if (!Objects.equals(pdf.getOriginalFilename(), "")) {
                //to save file the for the resume
            	FileUploadUtil.saveFile(uploadDir, resumeName, pdf);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        return "redirect:/dashboard/";
  }
	
	//download resume
	 @GetMapping("/{id}")
	    public String candidateProfile(@PathVariable("id") int id, Model model) {

	        Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(id);
	        model.addAttribute("profile", seekerProfile.get());
	        return "job-seeker-profile";
	    }

	    @GetMapping("/downloadResume")
	    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "userID") String userId) {

	        FileDownloadUtil downloadUtil = new FileDownloadUtil();
	        Resource resource = null;

	        try {
	            resource = downloadUtil.getFileAsResourse("photos/candidate/" + userId, fileName);
	        } catch (IOException e) {
	            return ResponseEntity.badRequest().build();
	        }

	        if (resource == null) {
	            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
	        }

	        String contentType = "application/octet-stream";
	        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
	                .body(resource);

	    }
}
