package com.portal.LoginPortal.ControllerTest;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.portal.LoginPortal.Controller.MainController;
import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.SecurityConfig.PasswordEncoder;
import com.portal.LoginPortal.Service.UserService;

@WebMvcTest(MainController.class)
@AutoConfigureMockMvc(addFilters = false)
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
		Optional<Userdata> userdataOptional = Optional.ofNullable(convertDtoToEntity(userdto));
		Userdata user = null;
		if(userdataOptional.isPresent()) {
			user = userdataOptional.get();
		}

		Mockito.when(userService.searchByUsername(userdto.getUsername())).thenReturn(userdata);
		Mockito.when(userService.searchByEmail(userdto.getEmail())).thenReturn(userdata);
		Mockito.when(userService.getUserDto(userdata)).thenReturn(userdto);
		Mockito.when(userService.getUserDto(user)).thenReturn(userdto);
		Mockito.when(userService.searchByUserId(5)).thenReturn(userdataOptional);
		


	}

	@Test
	public void test_addUser() throws Exception {
		UserDto user = new UserDto();
		user.setUsername("Alok2000");
		user.setEmail("alokranjan.sahoo@gmail.com");
		user.setPassword("Alok@2000");
		user.setNumber("8599069368");
		user.setAddress("Manyata");
		user.setCity("Bangalore");
		user.setState("Karnataka");
		user.setQuestion("What is your childhood name?");
		user.setAnswer("gudu");
		user.setZip("560045");
		user.setGender("male");

		Userdata userdata = convertDtoToEntity(user);
		Mockito.when(userService.add(user)).thenReturn(userdata);

		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/addUser").flashAttr("user", user));

		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("login"));
	}

	@Test
	public void test_updatePassword() throws Exception {
		UserDto user = new UserDto();
		user.setUsername("Alok2000");
		user.setEmail("alokranjan.sahoo20@gmail.com");
		user.setQuestion("What is your childhood name?");
		user.setAnswer("gudu");

//		This is going to send the data as html body oject and not Model attribute th-object . If you want 
//		to use this method, you need to write @RequestBody instead of @ModelAttribute in main controller

		/*
		 * ResultActions resultActions =
		 * this.mockMvc.perform(MockMvcRequestBuilders.post("/updatepassword")
		 * .contentType(MediaType.APPLICATION_JSON) .content(new
		 * ObjectMapper().writeValueAsString(user)));
		 */

//      You can use this method as an alternative also	

		/*
		 * ResultActions resultActions =
		 * this.mockMvc.perform(MockMvcRequestBuilders.post("/updatepassword")
		 * .param("username", "Alok2000").param("email", "alokranjan.sahoo20@gmail.com")
		 * .param("question", "What is your childhood name?").param("answer", "gudu"));
		 */

		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/updatepassword").flashAttr("user", user));

		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("forgotPasswordChange"));

	}

	@Test
	public void test_userDetails() throws Exception {
		UserDto user = new UserDto();
		user.setData("Alok2000");
		user.setPassword("Alok@2000");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/userDetails").flashAttr("user", user))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("userDetails"));

	}

	@Test
	public void test_setPassword() throws Exception {

		// we can also write like this: MockMvcRequestBuilders.post("/setPassword/1")
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/setPassword/{id}", "5").param("oldpassword", "Alok@2000")
						.param("newpassword", "Alok@2000"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("login"));
	}

	@Test
	public void test_changeForgotPassword() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/changeForgotPassword/{id}", "5").param("password", "Alok@2000")
						.param("password2", "Alok@2000"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("login"));
	}

	@Test
	public void test_deleteAccount() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteAccount/{id}", "5"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("login"));
	}
	
	@Test
	public void test_presentUserDetails() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/presentUserDetails/{id}","5"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("userDetails"));
	}

	private Userdata convertDtoToEntity(UserDto userdto) {
		Userdata userdata = new Userdata();
		String encryptedPassword = passwordEncoder.passwordEncode().encode(userdto.getPassword());
		userdata.setUsername(userdto.getUsername());
		userdata.setUserid(5);
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
		userdata.setLastLogin("2022-12-04 15:35");

		return userdata;
	}
}