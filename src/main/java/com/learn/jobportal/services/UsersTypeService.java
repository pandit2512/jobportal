package com.learn.jobportal.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.jobportal.entity.UsersType;
import com.learn.jobportal.repository.UsersTypeRepository;

@Service
public class UsersTypeService {

	private final UsersTypeRepository usersTypeRepository;

	public UsersTypeService(UsersTypeRepository usersTypeRepository) {
		
		this.usersTypeRepository=usersTypeRepository;
	}
	
	public List<UsersType> getAll(){
		
		return usersTypeRepository.findAll();
	}
}
