package com.portal.LoginPortal.Model;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user", schema = "Userdata")
@DynamicUpdate

public class Userdata {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer userid;

	@Column(name = "user_name")
	private String username;

	@Column(name = "user_email")
	private String email;

	@Column(name = "user_password")
	private String password;

	@Column(name = "gender")
	private String gender;

	@Column(name = "security_question")
	private String question;

	@Column(name = "seq_answer")
	private String answer;

	@Column(name = "mobile_number")
	private Integer number;

	@Column(name = "last_login_time")
	private String lastLogin;

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Userdata(Integer userid, String username, String email, String password, String gender, String question,
			String answer, Integer number, String lastLogin) {
		super();
		this.userid = userid;
		this.username = username;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.question = question;
		this.answer = answer;
		this.number = number;
		this.lastLogin = lastLogin;
	}

	@Override
	public String toString() {
		return "Userdata [userid=" + userid + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", gender=" + gender + ", question=" + question + ", answer=" + answer + ", number=" + number
				+ ", lastLogin=" + lastLogin + "]";
	}

	public Userdata() {
		super();
	}

}