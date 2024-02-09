package com.mvcguru.risiko.maven.eclipse.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.mvcguru.risiko.maven.eclipse.*;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.service.UserRepository;

@RestController
public class AuthController {
	
	@PostMapping("/login")
	public void loginUser(@RequestBody User user) throws UserException, DatabaseConnectionException{
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		User loggedUser = UserRepository.getInstance().getUser(user);
		if(loggedUser == null)
			System.out.println("Scemo");
		else
			System.out.println("Funzia");
	}
	
	@PostMapping("/register")
	public void registerUser(@RequestBody User user) throws UserException, DatabaseConnectionException{
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		UserRepository.getInstance().registerUser(user);
		
	}
}
