package com.learn.jobportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.jobportal.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {

	//this Spring Data jpa will give common CRUD functionality

   Optional<Users> findByEmail(String email);
}
