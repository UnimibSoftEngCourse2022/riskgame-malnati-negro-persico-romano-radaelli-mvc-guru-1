package com.mvcguru.risiko.maven.eclipse.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;
import lombok.Data;

@Data
public class FactoryGame {
	private static final Logger LOGGER = LoggerFactory.getLogger(FactoryGame.class);

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

    public static String creaId() {
         idGame = UUID.randomUUID().toString();
        return idGame;
    }


    public IGame createGame(GameConfiguration configuration) {
    
        IGame game = new Game(creaId(), configuration);     
        game.setState(LobbyState.builder().game(game).build());
        return game;
    }
    
}
