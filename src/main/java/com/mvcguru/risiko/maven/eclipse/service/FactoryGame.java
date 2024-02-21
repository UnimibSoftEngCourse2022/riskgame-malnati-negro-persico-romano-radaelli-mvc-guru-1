package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Turn;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.objectives.ConquerContinentObjective;
import com.mvcguru.risiko.maven.eclipse.model.card.objectives.DestroyArmyObjective;
import com.mvcguru.risiko.maven.eclipse.model.card.objectives.TerritoriesObjective;
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
        /*
        ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource(fileName).getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        
        ObjectiveCard[] objectives = mapper.readValue(json, ObjectiveCard[].class);
        */
        
        ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource(fileName).getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);

        // Deserializza il JSON in una lista di mappe
        List<Map<String,Object>> objectivesList = mapper.readValue(json, new TypeReference<List<Map<String,Object>>>(){});
        
        List<ObjectiveCard> objectives = new ArrayList<>();

        for (Map<String, Object> map : objectivesList) {
            String type = (String) map.get("type");
            ObjectiveCard objective = null;
            
            switch (type) {
                case "conquerContinent":
                	ConquerContinentObjective objective1 = mapper.convertValue(map, ConquerContinentObjective.class);
                	objective = objective1;
                    break;
                case "destroyArmy":
                	DestroyArmyObjective objective2 = mapper.convertValue(map, DestroyArmyObjective.class);
                    objective = objective2;
                    break;
                case "territories":
                	TerritoriesObjective objective3 = mapper.convertValue(map, TerritoriesObjective.class);
                    objective = objective3;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown type: " + type);
            }
            
            if (objective != null) {
                String description = (String) map.get("objective");
                //LOGGER.info("Objective: {}", description);
                objective.setObjective(description); // Assicurati che ci sia un metodo setDescription nella classe ObjectiveCard
                objectives.add(objective);
            }
        }

        
        IDeck deck = new ObjectivesDeck();
        for (ObjectiveCard o : objectives) {
            deck.insertCard(o);
        }
        return deck;
    }

    private String getFileNameForContinentGameMode(GameMode mode) {
        switch (mode) {
            case EASY: return "continent_easy.json";
            case MEDIUM: return "continent_medium.json";
            case HARD: return "continent_hard.json";
            default: return null;
        }
    }


    public IGame createGame(GameConfiguration configuration) throws IOException {
    
        IGame game = new Game(createId(), configuration);
        
        game.setDeckTerritory(createTerritoryDeck(configuration));
        game.setDeckObjective(createObjectiveDeck(configuration));
        game.setContinents(parsingContinent(configuration));
        game.setState(LobbyState.builder().game(game).build());
        return game;
    }
    
    private String getFileNameForObjectiveGameMode(GameMode mode) {
        switch (mode) {
            case EASY: return "objectives_easy.json";
            case MEDIUM: return "objectives_medium.json";
            case HARD: return "objectives_hard.json";
            default: return null;
        }
    }
    
    public List<Continent> parsingContinent(GameConfiguration configuration) throws IOException {
    	String fileName = getFileNameForContinentGameMode(configuration.getMode());
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource(fileName).getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        Continent[] continents = mapper.readValue(json, Continent[].class);
        return new ArrayList<>(Arrays.asList(continents));
	}
    
}
