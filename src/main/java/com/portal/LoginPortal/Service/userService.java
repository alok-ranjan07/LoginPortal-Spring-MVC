package com.portal.LoginPortal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.Repository.userRepository;



@Service
public class userService {
	
	@Autowired
	private userRepository userRepo;

	public void add(Userdata user) {
		userRepo.save(user);
		
	}

	public Userdata searchByUsername(String username) {
		
		return userRepo.findByUsername(username);
	}

	public Userdata searchByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepo.findByEmail(email);
	}


	
}