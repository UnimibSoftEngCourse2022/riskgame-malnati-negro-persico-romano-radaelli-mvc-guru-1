package com.mvcguru.risiko.maven.eclipse.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

@RestController
public class GameController {
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("/partita")
	public ResponseEntity<Void> gameCreation(@RequestBody String body) throws IOException {
		 GameConfiguration configuration = objectMapper.readValue(body, GameConfiguration.class);		
		if(!checkConfiguration(configuration)) {
			return ResponseEntity.badRequest().build();
		}
		
		IGame nuovaPartita = FactoryGame.getInstance().creaPartita(configuration);
		System.out.println("Partita creata con successo" + nuovaPartita.getId());
        return ResponseEntity.ok().build();
    }
	
	private boolean checkConfiguration(GameConfiguration configuration) {
		if (configuration.getMode() == null || configuration.getNumberOfPlayers() == 0
				|| configuration.getIdMap() == null)
			return false;
		return true;
	}
}