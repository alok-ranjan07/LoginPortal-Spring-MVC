package com.portal.LoginPortal.Dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDto {
	
	@Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "username must be of 6 to 12 length with no special characters")
	private String username;
	
	@Pattern(regexp = "[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?",
			message = "Please provide a valid email-id")
	private String email;
	
	@Pattern(regexp = "^[a-zA-Z0-9]{6,12}$"+"|"+"[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?",
			message = "Provide a valid Username or Email")
	private String data;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$",
            message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
	private String password;
	
	@Pattern(regexp = "^(0|91)?[7-9][0-9]{9}$",
			message = "Please provide a valid mobile number.")
	private String number;
	
	@Pattern(regexp = "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)",
			message = "Incorrect City Format")
	private String city;
	
	@Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$",
			message = "Wrong zip-code")
	private String zip;
	
	private String state;
	private String address;
	private String address2;
	private String gender;
	private String question;
	private String answer;
	private String lastLogin;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public UserDto(
			@Pattern(regexp = "^[a-zA-Z0-9]{6,12}$", message = "username must be of 6 to 12 length with no special characters") String username,
			@Pattern(regexp = "[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?", message = "Please provide a valid email-id") String email,
			@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$", message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ") String password,
			@Pattern(regexp = "^(0|91)?[7-9][0-9]{9}$", message = "Please provide a valid mobile number.") String number,
			@Pattern(regexp = "^[a-zA-Z0-9]{6,12}$|[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-A-Za-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[-A-Za-z0-9]*[A-Za-z0-9])?", message = "Provide a valid Username or Email") String data,
			@Pattern(regexp = "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)", message = "Incorrect City Format") String city,
			@Pattern(regexp = "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)", message = "Incorrect State Format") String state,
			@Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0, 1}[0-9]{3}$", message = "Wrong zip-code") String zip,
			String address, String address2, String gender, String question, String answer, String lastLogin) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.number = number;
		this.data = data;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.address = address;
		this.address2 = address2;
		this.gender = gender;
		this.question = question;
		this.answer = answer;
		this.lastLogin = lastLogin;
	}
	public UserDto() {
		super();
	}
	@Override
	public String toString() {
		return "UserDto [username=" + username + ", email=" + email + ", password=" + password + ", number=" + number
				+ ", data=" + data + ", city=" + city + ", state=" + state + ", zip=" + zip + ", address=" + address
				+ ", address2=" + address2 + ", gender=" + gender + ", question=" + question + ", answer=" + answer
				+ ", lastLogin=" + lastLogin + "]";
	}
	
	
	
}
