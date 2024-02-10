package com.mvcguru.risiko.maven.eclipse.service;

import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;

public class FactoryGame {

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
        IGame partita = new Game(creaId(), configuration);
        partita.setState(new LobbyState());
        return partita;
    }
}
