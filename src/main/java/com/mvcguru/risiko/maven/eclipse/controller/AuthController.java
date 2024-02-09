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
    public ResponseEntity<String> loginUser(@RequestBody User user){
        User loggedUser;
		try {
			loggedUser = UserRepository.getInstance().getUser(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore interno"); 
		}
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non esistente");
        } else {
            return ResponseEntity.ok("Login riuscito"); // 200 OK
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
    	try {
    		UserRepository.getInstance().registerUser(user);
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la registrazione");
    	}
        return ResponseEntity.ok("Registrazione riuscita");
    }
}