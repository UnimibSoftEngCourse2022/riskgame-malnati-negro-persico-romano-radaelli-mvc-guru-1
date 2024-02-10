package com.mvcguru.risiko.maven.eclipse.model.player;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testPlayerConstructor() {
        String testName = "Bobby";
        int testPlayerId = 42;

        Player player = new Player(testName, testPlayerId);

        assertEquals(testName, player.getName(), "Il nome del giocatore deve corrispondere a quello impostato nel costruttore");
        assertEquals(testPlayerId, player.getPlayerId(), "L'ID del giocatore deve corrispondere a quello impostato nel costruttore");
    }
}

