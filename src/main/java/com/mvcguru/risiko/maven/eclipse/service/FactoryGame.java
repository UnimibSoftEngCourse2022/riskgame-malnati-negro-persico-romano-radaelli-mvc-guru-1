package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static FactoryGame instance;
    private GameState gameState;

    public FactoryGame() {
        // Costruttore privato per impedire l'istanziazione esterna
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
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("territories_difficult.json").getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        TerritoryCard[] territoriesCard = mapper.readValue(json, TerritoryCard[].class);
        
        IDeck deck = new TerritoriesDeck();
		for (TerritoryCard territoryCard : territoriesCard) {
	            deck.insertCard(territoryCard);
			}
		return deck;
        }

	public IDeck createObjectiveDeck(GameConfiguration configuration) throws IOException {
		GameMode mode = configuration.getMode();
		ObjectMapper mapper = new ObjectMapper();
		byte[] data = null;
		switch (mode) {
			case EASY:
				data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives_easy.json").getInputStream());
				break;
			case MEDIUM:
				data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives_medium.json").getInputStream());
				break;
            case HARD:
				data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives_hard.json").getInputStream());
				break;
			default:
				break;
		}
        
        String json = new String(data, StandardCharsets.UTF_8);
        ObjectiveCard[] objectives = mapper.readValue(json, ObjectiveCard[].class);
        IDeck deck = new ObjectivesDeck();
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
