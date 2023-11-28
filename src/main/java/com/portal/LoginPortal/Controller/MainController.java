package com.portal.LoginPortal.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.Service.userService;

import jakarta.validation.Valid;

@Controller
public class MainController {

	@Autowired
	private userService userservice;

	String currentUserEmail;

	@GetMapping("/")
	public String show(Model model) {
		currentUserEmail = null;
		UserDto user = new UserDto();
        model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("signup")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "signup";
    }

	@GetMapping("/login")
	public String loginHandler(Model model) {
		currentUserEmail = null;
		UserDto user = new UserDto();
        model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("/logout")
	public String logoutHandler(Model model) {

		currentUserEmail = null;
		UserDto user = new UserDto();
		model.addAttribute("message", "Successfully Logout!");
		model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("/userDetails")
	public String profile() {
		return "UserDetails";
	}

	@GetMapping("/changePassword")
	public String changePasswordHandler() {
		return "ChangePassword";
	}

	@GetMapping("/forgotPassword")
	public String forgotPasswordHandler(Model model) {
		currentUserEmail = null;
		UserDto user = new UserDto();
        model.addAttribute("user", user);
		return "forgotPassword";
	}

	@GetMapping("/forgotPasswordChange")
	public String forgotPasswordChangeHandler() {
		return "ForgotPasswordChange";
	}
	
	@GetMapping("/presentUserDetails")
	public String currentUser(Model model) {
		Userdata currentUser = userservice.searchByEmail(currentUserEmail);
		UserDto user = userservice.getUserDto(currentUser);
		model.addAttribute("user", user);
		return "userDetails";
	}

	@PostMapping("/addUser")
	public String register(@Valid @ModelAttribute("user") UserDto userdto,
            BindingResult result,
            Model model, 
			RedirectAttributes redirectAttributes) {
		 if (result.hasErrors()) {
	            model.addAttribute("user", userdto);
	            return "signup";
	        }else {
			Userdata currentUserEmail = userservice.searchByEmail(userdto.getEmail());
			Userdata currentUserName = userservice.searchByUsername(userdto.getUsername());
			if (currentUserEmail == null && currentUserName == null) {
				
				Date dateNow = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
				String formatted = format.format(dateNow);
				userdto.setLastLogin(formatted);
				userservice.add(userdto);

				Integer userId = (userdto.getUsername() == null) ?

						userservice.searchById(userdto.getEmail()) : userservice.searchById(userdto.getUsername());
				String message = "Registration Successfully completed. Your User Id is : " + userId;
				redirectAttributes.addFlashAttribute("message", message);
				redirectAttributes.addFlashAttribute("userId", userId);
				return "redirect:/login";
			} else {
				String message = "Already registered! Please login.";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/login";
			}
		}
	}

	@PostMapping("/updatepassword")
	public String updatePassword(@Valid @ModelAttribute("user") UserDto userdto,
			BindingResult result,
			Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("user", userdto);
			return "forgotPassword";
		} else {
		Userdata currentUser = userservice.searchByUsername(userdto.getUsername());

		if (userdto.getEmail().equals(currentUser.getEmail()) && userdto.getQuestion().equals(currentUser.getQuestion())
				&& userdto.getAnswer().equals(currentUser.getAnswer())) {
			currentUserEmail = currentUser.getEmail();
			return "forgotPasswordChange";
		} else if (!(userdto.getEmail().equals(currentUser.getEmail()))) {
			model.addAttribute("message", "Email-id doesn't match.Please try again!");
			return "forgotPassword";
		} else if (!(userdto.getQuestion().equals(currentUser.getQuestion()))) {
			model.addAttribute("message", "Security question doesn't match. Please try again!");
			return "forgotPassword";
		} else if (!(userdto.getAnswer().equals(currentUser.getAnswer()))) {
			model.addAttribute("message", "Security Answer doesn't match. Please try again!");
			return "forgotPassword";
		}

		return "login";
		}
	}

	@PostMapping("/changeForgotPassword")
	public String changePasswordHandler(@RequestParam("password") String password,
			@RequestParam("password2") String password2, Model model, RedirectAttributes redirectAttributes) {
		Userdata userdata = userservice.searchByEmail(currentUserEmail);
		Date dateNow = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String formatted = format.format(dateNow);
		
		if (password.equals(password2)) {
			if(password.equals(userdata.getPassword())){
				String message = "Password can't be same as previous one";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/forgotPasswordChange";
			}else {
			userdata.setLastLogin(formatted);
			userdata.setPassword(password);
			userservice.add(userdata);
			String message = "Password changed successfully. Please login!";
			redirectAttributes.addFlashAttribute("message", message);

			return "redirect:/login";
			}
		}

		String message = "Please type the password again!";
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/forgotPasswordChange";
	}

	@PostMapping("/setPassword")
	public String change(@RequestParam("oldpassword") String oldPassword,
			@RequestParam("newpassword") String newPassword, Model model) {
		Userdata user = userservice.searchByEmail(currentUserEmail);
		Date dateNow = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String formatted = format.format(dateNow);

		if (oldPassword.equals(user.getPassword())) {
			user.setLastLogin(formatted);
			user.setPassword(newPassword);
			userservice.add(user);
			model.addAttribute("message", "Password changed succesfully");
			return "login";
		}

		model.addAttribute("message", "Incorrect Password! Please Try again...");
		return "changePassword";
	}

	@PostMapping("/userDetails")
	public String loginProcess(@Valid @ModelAttribute("user") UserDto userdto,
			BindingResult result,
			Model model,  RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("user", userdto);
			return "login";
		} else {
			if ( !userdto.getData().isEmpty() && !userdto.getPassword().isEmpty()) {
				Userdata usersName = userservice.searchByUsername(userdto.getData());
				Userdata usersEmail = userservice.searchByEmail(userdto.getData());
				Userdata userdata = null;

				while (usersName != null) {
					userdata = usersName;
					break;
				}
				while (usersEmail != null) {
					userdata = usersEmail;
					break;
				}

				if (userdata != null) {

					if (userdto.getPassword().equals(userdata.getPassword())) {
						Date date = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
						String newLoginTime = dateFormat.format(date);
						String lastLoginTime = userdata.getLastLogin().substring(0, 10);
						LocalDate newLogin = LocalDate.parse(newLoginTime);
						LocalDate lastLogin = LocalDate.parse(lastLoginTime);
						long diffInDays = ChronoUnit.DAYS.between(lastLogin, newLogin);
						currentUserEmail = userdata.getEmail();
						if (diffInDays <= 180) {
							Date dateNow = new Date();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
							String formatted = format.format(dateNow);
							userdata.setLastLogin(formatted);
							userservice.add(userdata);
							
							UserDto userDto = userservice.getUserDto(userdata);

							model.addAttribute("user", userDto);
						
							return "userDetails";
						} else {
							String message = "Your Account is inactive for more than 30 days! Please change your password.";
							redirectAttributes.addFlashAttribute("message", message);
							return "redirect:/changePassword";
						}
					} else {
						String message = "Username/Email-id and Password doesn't match. Please try again!";
						redirectAttributes.addFlashAttribute("message", message);
						return "redirect:/login";
					}

				}
				String message = "User doesn't exist! Please sign-up";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/signup";

			}  else {
				String message = "Something went wrong.";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/login";
			}
		}
	}

}