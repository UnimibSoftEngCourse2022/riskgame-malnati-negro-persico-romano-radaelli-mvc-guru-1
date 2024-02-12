package com.mvcguru.risiko.maven.eclipse.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;

class GameConfigurationTest {
    private GameConfiguration gameConfiguration;

    @BeforeEach
    void setUp() {
        // Creazione di un'istanza di GameConfiguration con un enum GameMode
        gameConfiguration = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(2)
                .idMap("mappa1")
                .build();
    }

    @Test
    void testGameConfiguration() {
        // Verifica che il GameMode sia stato impostato correttamente nel setup
        assertEquals(GameMode.EASY.toString(), gameConfiguration.getMode().toString());
        
        // Altre asserzioni per verificare gli altri attributi
        assertEquals(2, gameConfiguration.getNumberOfPlayers());
        assertEquals("mappa1", gameConfiguration.getIdMap());
    }
}
