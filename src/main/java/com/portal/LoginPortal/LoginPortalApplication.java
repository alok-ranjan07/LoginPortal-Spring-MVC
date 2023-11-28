package com.portal.LoginPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class LoginPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginPortalApplication.class, args);
	}

}