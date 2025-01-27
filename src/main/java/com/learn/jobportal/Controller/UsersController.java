package com.learn.jobportal.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.learn.jobportal.entity.Users;
import com.learn.jobportal.entity.UsersType;
import com.learn.jobportal.services.UsersService;
import com.learn.jobportal.services.UsersTypeService;

import jakarta.validation.Valid;

@Controller
public class UsersController {

	@Autowired
	private final UsersTypeService usersTypeService;
	
	@Autowired
	private final UsersService usersService;
	
	public UsersController(UsersTypeService usersTypeService,UsersService usersService) {
		
		this.usersTypeService=usersTypeService;
	    this.usersService=usersService;
	}
	
	//to show user registration form
	//learn about Model
	
	@GetMapping("/register")
	public String register(Model model) {
		
		List<UsersType> usersTypes=usersTypeService.getAll();
	
		model.addAttribute("getAllTypes",usersTypes);
		model.addAttribute("user", new Users());
		
		return "register";
	}
	
	@PostMapping("/register/new")
	public String userRegistration(@Valid Users users, Model model) {
		
		//System.out.println("User:: "+users);
		
		
		Optional<Users> optionalUsers = usersService.getUserByEmail(users.getEmail());
		if(optionalUsers.isPresent()) {
			model.addAttribute("error","Email already registered, try to login or register with other email.");
		
			List<UsersType> usersTypes=usersTypeService.getAll();
			
			model.addAttribute("getAllTypes",usersTypes);
			model.addAttribute("user", new Users());
			
			return "register";
		
		}
		
		
		
		usersService.addNew(users);
		
		return "dashboard";
	}
}
