package com.learn.jobportal.Controller;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.learn.jobportal.entity.RecruiterProfile;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.repository.UsersRepository;
import com.learn.jobportal.services.RecruiterProfileService;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

	private final UsersRepository usersRepository;
	private final RecruiterProfileService recruiterProfileService;
	
	
	public RecruiterProfileController(UsersRepository usersRepository,RecruiterProfileService recruiterProfileService) {
		
		this.usersRepository = usersRepository;
	     this.recruiterProfileService=recruiterProfileService;
	}

	@GetMapping("/")
	public String recruiterProfile(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	 if(!(authentication instanceof AnonymousAuthenticationToken)) {
		 String currentUsername=authentication.getName();
	     Users users = usersRepository.findByEmail(currentUsername).orElseThrow(()-> new 
	    		      UsernameNotFoundException("Could not "+ "found user "));
	     
	     Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());
	      
	     if(!recruiterProfile.isEmpty()) 
	    	 model.addAttribute("profile",recruiterProfile.get());
	         
	 }
	 return "recruiter_profile";
	 // this typo should read "recruiter_profile"
	}
	
	//-----------V-24: recruiter profile add----
    public String addNew(RecruiterProfile recruiterProfile,@RequestParam("image") 
                        MultipartFile multipartFile, Model model) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	if(!(authentication instanceof AnonymousAuthenticationToken)) {
    		
    		String currentUsername=authentication.getName();
    		
    		Users users = usersRepository.findByEmail(currentUsername).orElseThrow(()-> new 
    				  UsernameNotFoundException("Could "+ "not found user"));
    	
    	//below code associate recruiter profile with existing user account	
    	recruiterProfile.setUserId(users);
    	recruiterProfile.setUserAccountId(users.getUserId());
    	}
    	//now will add that profile to model
    	model.addAttribute("profile",recruiterProfile);
    	
    	//below code for processing the image/file upload
    	String fileName="";
    	if(!multipartFile.getOriginalFilename().equals("")) {
    		
    		 fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile
    				              .getOriginalFilename()));
    	
        //sets image name in recruiter profile
    	recruiterProfile.setProfilePhoto(fileName);
    	}
    	//below code: addNew()--> save recruiter profile to dataBase
    	RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);
    	
    	//setting the upoad directory
    	String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
    	
    	return null;
    }
}

