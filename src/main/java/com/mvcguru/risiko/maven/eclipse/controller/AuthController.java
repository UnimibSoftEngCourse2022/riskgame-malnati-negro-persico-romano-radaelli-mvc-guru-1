package com.mvcguru.risiko.maven.eclipse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.service.UserRepository;

@RestController
public class AuthController {
    
	@PostMapping("/login")
	public ResponseEntity<Void> loginUser(@RequestBody User user){
	    User loggedUser;
	    try {
	        loggedUser = UserRepository.getInstance().getUser(user);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Corretto per non includere un messaggio
	    }
	    if (loggedUser == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    } else {
	        return ResponseEntity.ok().build(); // 200 OK senza corpo
	    }
	}

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user){
    	System.out.println("dentro register");
    	try {
    		UserRepository.getInstance().registerUser(user);
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    	}
    	System.out.println("tutto bene"); 
    	return ResponseEntity.ok().build();
    }
}