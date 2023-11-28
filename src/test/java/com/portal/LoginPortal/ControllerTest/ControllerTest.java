package com.portal.LoginPortal.ControllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.LoginPortal.Controller.MainController;
import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.SecurityConfig.PasswordEncoder;
import com.portal.LoginPortal.Service.UserService;

@WebMvcTest(MainController.class)
public class ControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

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

		Mockito.when(userService.searchByUsername(userdto.getUsername())).thenReturn(userdata);
		Mockito.when(userService.searchByEmail(userdto.getEmail())).thenReturn(userdata);
	}
	

	@Test
	public void test_forgotPassword() throws Exception {
		UserDto user = new UserDto();
		user.setUsername("Alok2000");
		ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/updatePassword")
									  		   	  .contentType(MediaType.APPLICATION_JSON)
									  		   	  .content(new ObjectMapper().writeValueAsString(user)));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
					 .andExpect(MockMvcResultMatchers.view().name("forgotPassword"));

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