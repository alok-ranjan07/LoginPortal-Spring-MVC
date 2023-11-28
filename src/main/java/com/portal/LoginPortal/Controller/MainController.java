package com.portal.LoginPortal.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.Service.userService;

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

	@GetMapping("/signup")
	public String registraionHandler(Userdata userdatda) {
		return "signup";
	}

	@GetMapping("/login")
	public String loginHandler(Userdata userdata) {
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
	public String register(@ModelAttribute("user") Userdata userdata, RedirectAttributes redirectAttributes) {
		Userdata currentUserEmail = userservice.searchByEmail(userdata.getEmail());
		Userdata currentUserName = userservice.searchByUsername(userdata.getUsername());
		if (currentUserEmail == null && currentUserName == null) {

			userservice.add(userdata);
			String userId = userdata.getUserid().toString();
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
		Userdata user = userservice.searchByEmail(currentUserEmail);
		Date dateNow = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String formatted = format.format(dateNow);

		if (password.equals(password2)) {
			user.setLastLogin(formatted);
			user.setPassword(password);
			userservice.add(user);
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
	public String loginProcess(@RequestParam("username") String data, @RequestParam("password") String password,
			Model model, RedirectAttributes redirectAttributes) {
		if (!data.isEmpty() && !password.isEmpty()) {
			Userdata usersName = userservice.searchByUsername(data);
			Userdata usersEmail = userservice.searchByEmail(data);
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

				if (password.equals(user.getPassword())) {
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
					String newLoginTime = dateFormat.format(date);
					String lastLoginTime = user.getLastLogin().substring(0, 10);
					LocalDate newLogin = LocalDate.parse(newLoginTime);
					LocalDate lastLogin = LocalDate.parse(lastLoginTime);
					long diffInDays = ChronoUnit.DAYS.between(lastLogin, newLogin);
					currentUserEmail = user.getEmail();
					if (diffInDays <= 30) {
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

		} else if (data.isEmpty()) {
			String message = "Username/Email-id is empty";
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/login";
		} else if (password.isEmpty()) {
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