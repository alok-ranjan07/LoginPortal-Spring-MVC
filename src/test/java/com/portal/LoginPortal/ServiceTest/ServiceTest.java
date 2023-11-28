package com.portal.LoginPortal.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.Repository.UserRepository;
import com.portal.LoginPortal.SecurityConfig.PasswordEncoder;
import com.portal.LoginPortal.Service.UserService;

@SpringBootTest
public class ServiceTest{
	
	@MockBean
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@BeforeEach
	void setup() {
		UserDto userdto = new UserDto();
		userdto.setUsername("Alok2000");
		userdto.setEmail("alokranjan@gmail.com");
		userdto.setPassword("Alok@2000");
		userdto.setNumber("8599069368");
		userdto.setAddress("Manyata");
		userdto.setCity("Bangalore");
		userdto.setState("Karnataka");
		userdto.setQuestion("What is your childhood name?");
		userdto.setAnswer("gudu");
		userdto.setZip("560045");
		userdto.setGender("male");

		Userdata userdata = convertDtoToEntity(userdto);
		
		Mockito.when(userRepo.findByUsername(userdto.getUsername())).thenReturn(userdata);
		Mockito.when(userRepo.findByEmail(userdto.getEmail())).thenReturn(userdata);
	}
	
	
	@Test
	public void test_SearchByUserName() {
		String name = "Alok2000";
		String password = "Alok@2000";
		Userdata user = userService.searchByUsername("Alok2000");
		
		assertEquals(name, user.getUsername());
		assertEquals(password, user.getPassword());
	}
	
	@Test
	public void test_SearchByUserEmail() {
		String email = "alokranjan@gmail.com";
		String name = "Alok2000";
		Userdata user = userService.searchByEmail("alokranjan@gmail.com");
		
		assertEquals(email, user.getEmail());
		assertEquals(name, user.getUsername());
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
	
}
