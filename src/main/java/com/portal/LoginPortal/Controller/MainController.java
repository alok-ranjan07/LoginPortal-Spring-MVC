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
import org.springframework.validation.Errors;
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
	public String show() {
		return "home";
	}

	@GetMapping("/home")
	public String showHome() {
		return "home";
	}

	@GetMapping("signup")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "signup";
    }

	@GetMapping("/login")
	public String loginHandler(Model model) {
		UserDto user = new UserDto();
        model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("/logout")
	public String logoutHandler(Model model) {

		currentUserEmail = null;
		model.addAttribute("message", "Successfully Logout!");
		return "home";
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
	public String forgotPasswordHandler() {
		return "forgotPassword";
	}

	@GetMapping("/forgotPasswordChange")
	public String forgotPasswordChangeHandler() {
		return "ForgotPasswordChange";
	}

	@PostMapping("/addUser")
	public String register(@Valid @ModelAttribute("user") UserDto user,
            BindingResult result,
            Model model, 
			RedirectAttributes redirectAttributes) {
		 if (result.hasErrors()) {
	            model.addAttribute("user", user);
	            return "signup";
	        }else {
			Userdata currentUserEmail = userservice.searchByEmail(user.getEmail());
			Userdata currentUserName = userservice.searchByUsername(user.getUsername());
			if (currentUserEmail == null && currentUserName == null) {
				
				Date dateNow = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
				String formatted = format.format(dateNow);
				user.setLastLogin(formatted);
				userservice.add(user);

				Integer userId = (user.getUsername() == null) ?

						userservice.searchById(user.getEmail()) : userservice.searchById(user.getUsername());
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
	public String updatePassword(@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("question") String question, @RequestParam("answer") String answer, Model model) {
		Userdata currentUser = userservice.searchByUsername(username);

		if (email.equals(currentUser.getEmail()) && question.equals(currentUser.getQuestion())
				&& answer.equals(currentUser.getAnswer())) {
			currentUserEmail = currentUser.getEmail();
			return "forgotPasswordChange";
		} else if (!(email.equals(currentUser.getEmail()))) {
			model.addAttribute("message", "Email-id doesn't match.Please try again!");
			return "forgotPassword";
		} else if (!(question.equals(currentUser.getQuestion()))) {
			model.addAttribute("message", "Security question doesn't match. Please try again!");
			return "forgotPassword";
		} else if (!(answer.equals(currentUser.getAnswer()))) {
			model.addAttribute("message", "Security Answer doesn't match. Please try again!");
			return "forgotPassword";
		}

		return "home";
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
			userdata.setLastLogin(formatted);
			userdata.setPassword(password);
			userservice.add(userdata);
			String message = "Password changed successfully. Please login!";
			redirectAttributes.addFlashAttribute("message", message);

			return "redirect:/login";
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
			if (((!userdto.getUsername().isEmpty()) || (!userdto.getEmail().isEmpty())) && !userdto.getPassword().isEmpty()) {
				Userdata usersName = userservice.searchByUsername(userdto.getUsername());
				Userdata usersEmail = userservice.searchByEmail(userdto.getEmail());
				Userdata user = null;

				while (usersName != null) {
					user = usersName;
					break;
				}
				while (usersEmail != null) {
					user = usersName;
					break;
				}

				if (user != null) {

					if (userdto.getPassword().equals(user.getPassword())) {
						Date date = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
						String newLoginTime = dateFormat.format(date);
						String lastLoginTime = user.getLastLogin().substring(0, 10);
						LocalDate newLogin = LocalDate.parse(newLoginTime);
						LocalDate lastLogin = LocalDate.parse(lastLoginTime);
						long diffInDays = ChronoUnit.DAYS.between(lastLogin, newLogin);
						currentUserEmail = user.getEmail();
						if (diffInDays <= 180) {
							Date dateNow = new Date();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
							String formatted = format.format(dateNow);
							user.setLastLogin(formatted);
							userservice.add(user);

							model.addAttribute("username", user.getUsername());
							model.addAttribute("email", user.getEmail());
							model.addAttribute("number", user.getNumber());
							model.addAttribute("gender", user.getGender());
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

			} else if (userdto.getUsername().isEmpty() || userdto.getEmail().isEmpty()) {
				String message = "Username/Email-id is empty";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/login";
			} else if (userdto.getPassword().isEmpty()) {
				String message = "Password is empty";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/login";
			} else {
				String message = "Something went wrong. Pl";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/home";
			}
		}
	}

}