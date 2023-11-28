package com.portal.LoginPortal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.Repository.UserRepository;
import com.portal.LoginPortal.SecurityConfig.PasswordEncoder;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void add(UserDto userdto) {

		userRepo.save(convertDtoToEntity(userdto));

	}

	public void add(Userdata userdata) {

		userRepo.save(userdata);

	}

	public void deleteUser(Userdata currentUser) {

		userRepo.delete(currentUser);

	}

	public UserDto getUserDto(Userdata user) {
		return convertEntityToDto(user);

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
		Userdata userdata = new Userdata();
		String encryptedPassword = passwordEncoder.passwordEncode().encode(userdto.getPassword());
		userdata.setUsername(userdto.getUsername());
		userdata.setEmail(userdto.getEmail());
		userdata.setNumber(userdto.getNumber());
		userdata.setAnswer(userdto.getAnswer());
		userdata.setGender(userdto.getGender());
		userdata.setPassword(encryptedPassword);
		userdata.setQuestion(userdto.getQuestion());
		userdata.setAddress(userdto.getAddress());
		userdata.setAddress2(userdto.getAddress2());
		userdata.setCity(userdto.getCity());
		userdata.setState(userdto.getState());
		userdata.setZip(userdto.getZip());
		userdata.setLastLogin(userdto.getLastLogin());

		return userdata;
	}

	private UserDto convertEntityToDto(Userdata userdata) {
		UserDto userdto = new UserDto();
		userdto.setUsername(userdata.getUsername());
		userdto.setEmail(userdata.getEmail());
		userdto.setNumber(userdata.getNumber());
		userdto.setAnswer(userdata.getAnswer());
		userdto.setGender(userdata.getGender());
		userdto.setPassword(userdata.getPassword());
		userdto.setQuestion(userdata.getQuestion());
		userdto.setLastLogin(userdata.getLastLogin());
		userdto.setAddress(userdata.getAddress());
		userdto.setAddress2(userdata.getAddress2());
		userdto.setCity(userdata.getCity());
		userdto.setState(userdata.getState());
		userdto.setZip(userdata.getZip());

		return userdto;
	}

}