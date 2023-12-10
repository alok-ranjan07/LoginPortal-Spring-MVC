package com.portal.LoginPortal.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;
import com.portal.LoginPortal.SecurityConfig.PasswordEncoder;
import com.portal.LoginPortal.Service.UserService;
import jakarta.validation.Valid;

@Controller
public class MainController {

	@Autowired
	private UserService userservice;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping(value = { "/", "/login" })
	public String show(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("signup")
	public String showRegistrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "signup";
	}

	@GetMapping("/logout")
	public String logoutHandler(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("message", "Successfully Logout!");
		model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("/userDetails")
	public String profile() {
		return "UserDetails";
	}

	@GetMapping(value = { "/changeUserPassword/{userid}" })
	public String changeUserPasswordHandler(@PathVariable(value = "userid") String userid,
			RedirectAttributes redirectAttributes) {
		Userdata currentUser = null;
		Optional<Userdata> userdata = userservice.searchByUserId(Integer.parseInt(userid));
		if (userdata.isPresent()) {
			currentUser = userdata.get();
		}
		redirectAttributes.addFlashAttribute("userid", userid);
		redirectAttributes.addFlashAttribute("username", currentUser.getUsername());
		return "redirect:/changePassword";
	}

	@GetMapping("/changePassword")
	public String changePasswordHandler() {
		return "changePassword";
	}

	@GetMapping("/forgotPassword")
	public String forgotPasswordHandler(Model model) {

		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "forgotPassword";
	}

	@GetMapping("/forgotPasswordChange")
	public String forgotPasswordChangeHandler() {
		return "ForgotPasswordChange";
	}

	@GetMapping("/presentUserDetails/{userid}")
	public String currentUser(Model model, @PathVariable("userid") String userid,
			RedirectAttributes redirectAttributes) {
		Userdata currentUser = null;
		Optional<Userdata> userdata = userservice.searchByUserId(Integer.parseInt(userid));
		if (userdata.isPresent()) {
			currentUser = userdata.get();
		}
		UserDto user = userservice.getUserDto(currentUser);
		redirectAttributes.addFlashAttribute("user", user);
		redirectAttributes.addFlashAttribute("userid", currentUser.getUserid());
		return "redirect:/userDetails";
	}

	@GetMapping("/deleteAccount/{id}")
	public String deleteAccountHandler(@PathVariable("id") String userid, RedirectAttributes redirectAttributes) {
		Userdata currentUser = null;
		Optional<Userdata> userdata = userservice.searchByUserId(Integer.parseInt(userid));
		if (userdata.isPresent()) {
			currentUser = userdata.get();
		}
		userservice.deleteUser(currentUser);
		String message = "Account Deleted Successfully! ";
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/login";

	}

	@PostMapping("/addUser")
	public String register(@Valid @ModelAttribute("user") UserDto userdto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("user", userdto);
			return "signup";
		} else {
			Userdata currentUserEmail = userservice.searchByEmail(userdto.getEmail());
			Userdata currentUserName = userservice.searchByUsername(userdto.getUsername());
			if (currentUserEmail == null && currentUserName == null) {

				Date dateNow = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
				String formatted = format.format(dateNow);
				userdto.setLastLogin(formatted);
				Userdata currentUser = userservice.add(userdto);
				Integer userId = currentUser.getUserid();
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
	public String updatePassword(@Valid @ModelAttribute("user") UserDto userdto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			model.addAttribute("user", userdto);
			return "forgotPassword";
		} else {
			Userdata usersName = userservice.searchByUsername(userdto.getUsername());
			Userdata usersEmail = userservice.searchByEmail(userdto.getEmail());
			Userdata currentUser = null;

			while (usersName != null) {
				currentUser = usersName;
				break;
			}
			while (usersEmail != null) {
				currentUser = usersEmail;
				break;
			}

			if (currentUser != null) {

				if (userdto.getEmail().equals(currentUser.getEmail())
						&& userdto.getUsername().equals(currentUser.getUsername())
						&& userdto.getQuestion().equals(currentUser.getQuestion())
						&& userdto.getAnswer().equals(currentUser.getAnswer())) {
					redirectAttributes.addFlashAttribute("userid", currentUser.getUserid());
					redirectAttributes.addFlashAttribute("username", currentUser.getUsername());
					return "redirect:/forgotPasswordChange";
				} else if (!(userdto.getUsername().equals(currentUser.getUsername()))) {
					model.addAttribute("message", "Username doesn't match.Please try again!");
					return "forgotPassword";
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
			} else {
				model.addAttribute("message", "Invalid user.Please try again!");
				return "forgotPassword";
			}

		}
		return "/";
	}

	@PostMapping("/changeForgotPassword/{id}")
	public String changePasswordHandler(@RequestParam("password") String password,
			@RequestParam("password2") String password2, @PathVariable("id") String userid, Model model,
			RedirectAttributes redirectAttributes) {
		Userdata userdata = null;
		Optional<Userdata> userData = userservice.searchByUserId(Integer.parseInt(userid));
		if (userData.isPresent()) {
			userdata = userData.get();
		}
		Date dateNow = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String formatted = format.format(dateNow);

		if (password.equals(password2)) {
			if (passwordEncoder.passwordEncode().matches(password, userdata.getPassword())) {
				String message = "Password can't be same as previous one";
				redirectAttributes.addFlashAttribute("message", message);
				redirectAttributes.addFlashAttribute("userid", userdata.getUserid());
				redirectAttributes.addFlashAttribute("username", userdata.getUsername());
				return "redirect:/forgotPasswordChange";
			} else {
				userdata.setLastLogin(formatted);
				userdata.setPassword(passwordEncoder.passwordEncode().encode(password));
				userservice.add(userdata);
				String message = "Password changed successfully. Please login!";
				redirectAttributes.addFlashAttribute("message", message);

				return "redirect:/login";
			}
		}

		String message = "Please type the password again!";
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("userid", userdata.getUserid());
		redirectAttributes.addFlashAttribute("username", userdata.getUsername());
		return "redirect:/forgotPasswordChange";
	}

	@PostMapping("/setPassword/{userid}")
	public String change(@RequestParam("oldpassword") String oldPassword,
			@RequestParam("newpassword") String newPassword, @PathVariable("userid") String userid, Model model,
			RedirectAttributes redirectAttributes) {
		Userdata user = null;
		Optional<Userdata> userdata = userservice.searchByUserId(Integer.parseInt(userid));
		if (userdata.isPresent()) {
			user = userdata.get();
		}
		Date dateNow = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String formatted = format.format(dateNow);

		if (passwordEncoder.passwordEncode().matches(oldPassword, user.getPassword())) {
			if (oldPassword.equals(newPassword)) {
				redirectAttributes.addFlashAttribute("message",
						"Password can not be same as previous one! Please Try again...");
				redirectAttributes.addFlashAttribute("userid", user.getUserid());
				redirectAttributes.addFlashAttribute("username", user.getUsername());
				return "redirect:/changePassword";
			} else {
				user.setLastLogin(formatted);
				user.setPassword(passwordEncoder.passwordEncode().encode(newPassword));
				userservice.add(user);
				String message = "Password changed succesfully! ";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/login";
			}

		} else {

			redirectAttributes.addFlashAttribute("message", "Incorrect Password! Please Try again...");
			redirectAttributes.addFlashAttribute("userid", user.getUserid());
			redirectAttributes.addFlashAttribute("username", user.getUsername());
			return "redirect:/changePassword";
		}
	}

	@PostMapping("/userDetails")
	public String loginProcess(@Valid @ModelAttribute("user") UserDto userdto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("user", userdto);
			return "login";
		} else {
			if (!userdto.getData().isEmpty() && !userdto.getPassword().isEmpty()) {
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

					if (passwordEncoder.passwordEncode().matches(userdto.getPassword(), userdata.getPassword())) {
						Date date = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
						String newLoginTime = dateFormat.format(date);
						String lastLoginTime = userdata.getLastLogin().substring(0, 10);
						LocalDate newLogin = LocalDate.parse(newLoginTime);
						LocalDate lastLogin = LocalDate.parse(lastLoginTime);
						long diffInDays = ChronoUnit.DAYS.between(lastLogin, newLogin);

						if (diffInDays <= 180) {

							UserDto userDto = userservice.getUserDto(userdata);
							model.addAttribute("user", userDto);
							model.addAttribute("userid", userdata.getUserid());

							Date dateNow = new Date();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
							String formatted = format.format(dateNow);
							userdata.setLastLogin(formatted);
							userservice.add(userdata);

							return "userDetails";
						} else {
							String message = "Your Account is inactive for more than 30 days! Please change your password.";
							redirectAttributes.addFlashAttribute("message", message);
							redirectAttributes.addFlashAttribute("username", userdata.getUsername());
							redirectAttributes.addFlashAttribute("userid", userdata.getUserid());
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

			} else {
				String message = "Something went wrong.";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/login";
			}
		}
	}

}