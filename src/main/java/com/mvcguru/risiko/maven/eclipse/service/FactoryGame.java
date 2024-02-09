package com.mvcguru.risiko.maven.eclipse.service;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.lang.module.Configuration;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FactoryGame {

    private static FactoryGame instance;
    private static int idPartita = 0;

    public static synchronized FactoryGame getInstance() {
        if (instance == null)
            instance = new FactoryGame();
        return instance;
    }

    public int creaId() {
        /*String idPartita = UUID.randomUUID().toString();
        while (GameRepository.getInstance().getPartitaById(idPartita) != null) {
            idPartita = UUID.randomUUID().toString();
        }*/
    	idPartita ++;
        return idPartita;
    }
    
    public IGame creaPartita(GameConfiguration configuration) throws IOException {

        IGame partita = new Game(creaId(), configuration);
        
        partita.setStato(new LobbyState());
        
        return partita;
    }

}

