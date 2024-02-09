package com.mvcguru.risiko.maven.eclipse.service;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FactoryGame {

    private static FactoryGame instance;

    public static synchronized FactoryGame getInstance() {
        if (instance == null)
            instance = new FactoryGame();
        return instance;
    }
/*
    public String creaId() {
        String idPartita = UUID.randomUUID().toString();
        while (GameRepository.getInstance().getPartitaById(idPartita) != null) {
            idPartita = UUID.randomUUID().toString();
        }
        return idPartita;
    }  */
    
    public IPartita creaPartita(Configurazione config) throws IOException {
        Tabellone tabellone = creaTabellone(config);
        IMazzo mazzo = creaMazzo(tabellone, config);

        IPartita partita = Partita.builder()
                .id(creaId())
                .tabellone(tabellone)
                .mazzo(mazzo)
                .config(config)
                .build();
        tabellone.setPartita(partita);
        List<Casella> caselle = tabellone.getCaselle();
        caselle.forEach(casella -> casella.aggiungi((PartitaObserver) partita));
        partita.setStato(Lobby.builder().build());
        return partita;
    }

}

