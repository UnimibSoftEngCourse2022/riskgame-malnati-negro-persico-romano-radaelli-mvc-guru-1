package com.mvcguru.risiko.maven.eclipse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

@RestController
public class GameController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("/partita")
	public ResponseEntity<String> gameCreation(@RequestBody String body) throws IOException {
		
		 GameConfiguration configuration = objectMapper.readValue(body, GameConfiguration.class);		
		if(checkConfiguration(configuration)) {
			return ResponseEntity.badRequest().build();
		}
		
		IGame nuovaPartita = FactoryGame.getInstance().createGame(configuration);
		
		try {
			GameRepository.getInstance().registerGame(nuovaPartita);
		} catch (Exception e) {
			LOGGER.error("Errore nella registrazione della partita", e);
			return ResponseEntity.badRequest().build();
		}
		
        return ResponseEntity.ok().body("{\"id\": \"" + nuovaPartita.getId() + "\"}");
    }
	
	@GetMapping("/partita")
	public ResponseEntity<List<IGame>> gameCreation() {
		List<IGame> lobbies;
		try {
			lobbies = GameRepository.getInstance().getAllGames();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(lobbies);
    }

	
	private boolean checkConfiguration(GameConfiguration configuration) {
		return configuration.getMode() == null || configuration.getNumberOfPlayers() == 0 || configuration.getIdMap() == null;
	}
}