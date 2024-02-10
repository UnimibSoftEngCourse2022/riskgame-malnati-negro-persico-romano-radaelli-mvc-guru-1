package com.mvcguru.risiko.maven.eclipse.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

@RestController
public class GameController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("/partita")
	public ResponseEntity<Void> gameCreation(@RequestBody String body) throws IOException {
		
		 GameConfiguration configuration = objectMapper.readValue(body, GameConfiguration.class);		
		if(!checkConfiguration(configuration)) {
			return ResponseEntity.badRequest().build();
		}
		
		LOGGER.info("Creazione partita: " + configuration);
		System.out.println("Creazione partita: " + configuration);
		
		IGame nuovaPartita = FactoryGame.getInstance().creaPartita(configuration);
		
		LOGGER.info("Partita creata: " + nuovaPartita.getId());
		System.out.println("Partita creata: " + nuovaPartita.getId());
		
        return ResponseEntity.ok().build();
    }
	
	@GetMapping("/partita")
	public ResponseEntity<String> gameCreation() throws IOException {
		 
        return ResponseEntity.ok("Partita creata");
    }

	
	private boolean checkConfiguration(GameConfiguration configuration) {
		if (configuration.getMode() == null || configuration.getNumberOfPlayers() == 0
				|| configuration.getIdMap() == null)
			return false;
		return true;
	}
}