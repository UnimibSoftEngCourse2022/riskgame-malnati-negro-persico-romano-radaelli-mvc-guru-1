package com.mvcguru.risiko.maven.eclipse.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.states.LobbyState;

class FactoryGameTest {

    @Test
    void testCreateGame() throws IOException {
        GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(4)
                .idMap("TestMap")
                .build();

        IGame game = FactoryGame.getInstance().createGame(config);
        
        assertNotNull(game);
        assertNotNull(game.getId());
        assertFalse(game.getId().isEmpty());
        assertEquals(GameMode.EASY, game.getConfiguration().getMode());
        assertEquals(4, game.getConfiguration().getNumberOfPlayers());
        assertEquals("TestMap", game.getConfiguration().getIdMap());
        assertTrue(game.getState() instanceof LobbyState);
    }
}
