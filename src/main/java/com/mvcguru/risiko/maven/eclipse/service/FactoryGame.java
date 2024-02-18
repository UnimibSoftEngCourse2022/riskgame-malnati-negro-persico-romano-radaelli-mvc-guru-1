package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.util.UUID;

import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FactoryGame {
    private static FactoryGame instance;
    private GameState gameState;


    public static synchronized FactoryGame getInstance() {
        if (instance == null)
            instance = new FactoryGame();
        return instance;
    }

    public static String createId() {
        return UUID.randomUUID().toString();
    }

    public IGame createGame(GameConfiguration configuration) throws IOException {
    
        IGame game = new Game(createId(), configuration);
        
        game.setDeckTerritory(game.createTerritoryDeck(configuration));
        game.setDeckObjective(game.createObjectiveDeck(configuration));
        game.setContinents(game.parsingContinent());
        
        game.setState(LobbyState.builder().game(game).build());
        
        return game;
    }
    
}
