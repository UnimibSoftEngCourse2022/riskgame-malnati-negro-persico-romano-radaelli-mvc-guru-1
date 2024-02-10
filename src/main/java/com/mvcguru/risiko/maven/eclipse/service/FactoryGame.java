package com.mvcguru.risiko.maven.eclipse.service;

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
    
    private static int idPartita = 0;

    private FactoryGame() {
        // Costruttore privato per impedire l'istanziazione esterna
    }

    public static synchronized FactoryGame getInstance() {
        if (instance == null)
            instance = new FactoryGame();
        return instance;
    }

    public static int creaId() {
        idPartita++;
        return idPartita;
    }


    public IGame creaPartita(GameConfiguration configuration) {
    	LOGGER.info("LobbyState: creazione partita - CONFIGURATION: " + configuration);
    	System.out.println("LobbyState: creazione partita - CONFIGURATION: " + configuration);
    	
        IGame partita = new Game(creaId(), configuration);
        LOGGER.info("LobbyState: creazione partita - partita creata " + partita.getId());
        System.out.println("LobbyState: creazione partita - partita creata " + partita.getId());
        
        partita.setState(new LobbyState());
        LOGGER.info("LobbyState: creazione partita - stato iniziale " + partita.getState().getClass().getSimpleName());
        System.out.println("LobbyState: creazione partita - stato iniziale " + partita.getState().getClass().getSimpleName());
        return partita;
    }
}
