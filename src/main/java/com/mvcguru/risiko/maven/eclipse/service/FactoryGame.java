package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.deck.ObjectivesDeck;
import com.mvcguru.risiko.maven.eclipse.model.deck.TerritoriesDeck;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;
import lombok.Data;

@Data
public class FactoryGame {
	private static final Logger LOGGER = LoggerFactory.getLogger(FactoryGame.class);
    private static FactoryGame instance;
    private GameState gameState;

	public FactoryGame() {
		//default constructor	
	}
    
    public static synchronized FactoryGame getInstance() {
        if (instance == null)
            instance = new FactoryGame();
        return instance;
    }

    public static String createId() {
        return UUID.randomUUID().toString();
    }
    
    public IDeck createTerritoryDeck(GameConfiguration configuration) throws IOException {
        GameMode mode = configuration.getMode();
        String fileName = getFileNameForGameMode(mode);
        if (fileName == null) {
        	LOGGER.error("Unsupported game mode: {}", mode);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource(fileName).getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        TerritoryCard[] territoriesCard = mapper.readValue(json, TerritoryCard[].class);
        
        IDeck deck = new TerritoriesDeck();
        for (TerritoryCard territoryCard : territoriesCard) {
            deck.insertCard(territoryCard);
        }
        return deck;
    }

    private String getFileNameForGameMode(GameMode mode) {
        switch (mode) {
            case EASY: return "territories_easy.json";
            case MEDIUM: return "territories_medium.json";
            case HARD: return "territories_hard.json";
            default: return null;
        }
    }


    public IDeck createObjectiveDeck(GameConfiguration configuration) throws IOException {
        GameMode mode = configuration.getMode();
        String fileName = getFileNameForObjectiveGameMode(mode);
        if (fileName == null) {
        	LOGGER.error("Unsupported game mode: {}", mode);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource(fileName).getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        ObjectiveCard[] objectives = mapper.readValue(json, ObjectiveCard[].class);
        
        IDeck deck = new ObjectivesDeck();
        for (ObjectiveCard o : objectives) {
            deck.insertCard(o);
        }
        return deck;
    }

    private String getFileNameForObjectiveGameMode(GameMode mode) {
        switch (mode) {
            case EASY: return "objectives_easy.json";
            case MEDIUM: return "objectives_medium.json";
            case HARD: return "objectives_hard.json";
            default: return null;
        }
    }


    public IGame createGame(GameConfiguration configuration) throws IOException {
    
        IGame game = new Game(createId(), configuration);
        
        game.setDeckTerritory(createTerritoryDeck(configuration));
        game.setDeckObjective(createObjectiveDeck(configuration));
        game.setContinents(parsingContinent());
        game.setState(LobbyState.builder().game(game).build());
        return game;
    }
    
    public List<Continent> parsingContinent() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("continent_hard.json").getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        Continent[] continents = mapper.readValue(json, Continent[].class);
        return new ArrayList<>(Arrays.asList(continents));
	}
    
}
