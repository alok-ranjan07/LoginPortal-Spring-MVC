package com.portal.LoginPortal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.Repository.userRepository;

@Service
public class userService {

	@Autowired
	private userRepository userRepo;

	public void add(UserDto userdto) {

		userRepo.save(convertDtoToEntity(userdto));

	}

	public void add(Userdata userdata) {

		userRepo.save(userdata);

	}

	public Userdata searchByUsername(String username) {

		return userRepo.findByUsername(username);
	}

	public Userdata searchByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public Integer searchById(String data) {
		if (userRepo.findByEmail(data) != null) {
			return userRepo.findByEmail(data).getUserid();
		} else {
			return userRepo.findByUsername(data).getUserid();
		}

	}

	private Userdata convertDtoToEntity(UserDto userdto) {
		Userdata user = new Userdata();
		user.setUsername(userdto.getUsername());
		user.setEmail(userdto.getEmail());
		user.setNumber(userdto.getNumber());
		user.setAnswer(userdto.getAnswer());
		user.setGender(userdto.getGender());
		user.setPassword(userdto.getPassword());
		user.setQuestion(userdto.getQuestion());
		user.setLastLogin(userdto.getLastLogin());

		return user;
	}

}