package com.mvcguru.risiko.maven.eclipse.model;

import static org.junit.jupiter.api.Assertions.*;
import com.mvcguru.risiko.maven.eclipse.controller.MessageBrokerSingleton;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GameTest {

    @InjectMocks
    private Game game;

    @Mock
    private MessageBrokerSingleton messageBroker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameConfiguration config = GameConfiguration.builder()
                .numberOfPlayers(2)
                .build();
        game = new Game("gameId", config);
    }

    @Test
    void testAddPlayer() throws FullGameException {
        Player player1 = new Player();
        player1.setUserName("player1");

        game.addPlayer(player1);

        assertEquals(1, game.getPlayers().size(), "Player should be added to the game");
        assertTrue(game.getPlayers().contains(player1), "The game should contain the added player");
    }

    @Test
    void testAddPlayer_FullGameException() {
        Player player1 = new Player();
        player1.setUserName("player1");
        Player player2 = new Player();
        player2.setUserName("player2");
        
        assertDoesNotThrow(() -> game.addPlayer(player1));
        assertDoesNotThrow(() -> game.addPlayer(player2));
        
        Player player3 = new Player();
        player3.setUserName("player3");

        assertThrows(FullGameException.class, () -> game.addPlayer(player3), "Should throw FullGameException when adding a player to a full game");
    }

    @Test
    void testFindPlayerByUsername() throws FullGameException {
        Player player1 = new Player();
        player1.setUserName("player1");
        game.addPlayer(player1);

        Player result = game.findPlayerByUsername("player1");

        assertNotNull(result, "Should find the player by username");
        assertEquals(player1.getUserName(), result.getUserName(), "Found player username should match");
    }

    @Test
    void testFindPlayerByUsername_NotFound() {
        Player result = game.findPlayerByUsername("nonExistingPlayer");
        assertNull(result, "Should return null when player does not exist");
    }
}
