package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.deck.ObjectivesDeck;
import com.mvcguru.risiko.maven.eclipse.model.deck.TerritoriesDeck;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;
import lombok.Data;

@Data
public class FactoryGame {
    private static FactoryGame instance;
    
    private static String idGame;


    private FactoryGame() {
        // Costruttore privato per impedire l'istanziazione esterna
    }

    public static synchronized FactoryGame getInstance() {
        if (instance == null)
            instance = new FactoryGame();
        return instance;
    }

    public static String createId() {
         idGame = UUID.randomUUID().toString();
        return idGame;
    }
    
	public static String getIdGame() {
		return idGame;
	}
	
	
	public static TerritoriesDeck createTerritoryDeck(GameConfiguration configuration) throws IOException {
				
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("territories.json").getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        Territory[] territories = mapper.readValue(json, Territory[].class);
        
        TerritoriesDeck deck = new TerritoriesDeck();
		for (Territory t : territories) {
			ICard territoryCard = new TerritoryCard(t);
            deck.insertCard(territoryCard);
        }
		return deck;
	}
	
	public static ObjectivesDeck createObjectiveDeck(GameConfiguration configuration) throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives.json").getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        ObjectiveCard[] objectives = mapper.readValue(json, ObjectiveCard[].class);
       
        ObjectivesDeck deck = new ObjectivesDeck();
		for (ObjectiveCard o : objectives) {
            deck.insertCard(o);
        }
		return deck;
	}
		


    public IGame createGame(GameConfiguration configuration) throws IOException {
    
        IGame game = new Game(createId(), configuration);    
        game.setDeckTerritory(createTerritoryDeck(configuration));
        game.setDeckObjective(createObjectiveDeck(configuration));
        game.setState(LobbyState.builder().game(game).build());
        
        return game;
    }
    
}
