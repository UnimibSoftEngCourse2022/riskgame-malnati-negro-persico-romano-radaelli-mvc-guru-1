package com.mvcguru.risiko.maven.eclipse.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;
import lombok.Data;

@Data
public class FactoryGame {
	private static final Logger LOGGER = LoggerFactory.getLogger(FactoryGame.class);

    private static FactoryGame instance;
    
    String idGame;


    private FactoryGame() {
        // Costruttore privato per impedire l'istanziazione esterna
    }

    public static synchronized FactoryGame getInstance() {
        if (instance == null)
            instance = new FactoryGame();
        return instance;
    }

    public static String creaId() {
        String idGame = UUID.randomUUID().toString();
        return idGame;
    }


    public IGame createGame(GameConfiguration configuration) {
    
        IGame partita = new Game(creaId(), configuration);     
        partita.setState(LobbyState.builder().game(partita).build());
        return partita;
    }
}
