package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.util.UUID;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;
import lombok.Data;

@Data
public class FactoryGame {
    private static FactoryGame instance;
    private GameState gameState;
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


    public IGame createGame(GameConfiguration configuration) throws IOException {
    
        IGame game = new Game(createId(), configuration);
        
        game.createTerritoryDeck(configuration);
        game.createObjectiveDeck(configuration);
        game.setState(LobbyState.builder().game(game).build());
        
        return game;
    }
    
}
