package com.mvcguru.risiko.maven.eclipse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mvcguru.risiko.maven.eclipse.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.service.UserRepository;

@RestController
public class GameController {
	
	@PostMapping("/partita")
	public ResponseEntity<Void> gameCreation(@RequestBody GameConfiguration configuration) {
		
		if(!checkConfiguration(configuration)) {
			return ResponseEntity.badRequest().build();
		}
		
		IGame nuovaPartita = FactoryGame.getInstance().creaPartita(configuration);
        
    }
	
	private boolean checkConfiguration(GameConfiguration configuration) {
		if (configuration.getMode() == null || configuration.getNumberOfPlayers() == 0
				|| configuration.getMap() == null)
			return false;
		return true;
	}
}
