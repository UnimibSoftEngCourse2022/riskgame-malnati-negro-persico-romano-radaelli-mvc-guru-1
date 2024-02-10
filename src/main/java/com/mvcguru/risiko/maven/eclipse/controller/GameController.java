package com.mvcguru.risiko.maven.eclipse.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

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
		
		try {
			GameRepository.getInstance().registerGame(nuovaPartita);
		} catch (Exception e) {
			System.out.println("Errore nella registrazione della partita");
			return ResponseEntity.badRequest().build();
		}
		
        return ResponseEntity.ok().build();
    }
	
	@GetMapping("/partita")
	public ResponseEntity<List<IGame>> gameCreation() throws IOException {
		List<IGame> lobbies;
		try {
			lobbies = GameRepository.getInstance().getAllGames();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
        }
		System.out.println("Partite: " + lobbies);
        return ResponseEntity.ok(lobbies);
    }

	
	private boolean checkConfiguration(GameConfiguration configuration) {
		if (configuration.getMode() == null || configuration.getNumberOfPlayers() == 0
				|| configuration.getIdMap() == null)
			return false;
		return true;
	}
}