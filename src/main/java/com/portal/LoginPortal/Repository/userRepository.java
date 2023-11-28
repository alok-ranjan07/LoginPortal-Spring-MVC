package com.portal.LoginPortal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.LoginPortal.Dto.UserDto;
import com.portal.LoginPortal.Model.Userdata;





@Repository
public interface userRepository extends JpaRepository<com.portal.LoginPortal.Model.Userdata, Integer> {

	Userdata findByUsername(String username);

	Userdata findByEmail(String email);

	
}