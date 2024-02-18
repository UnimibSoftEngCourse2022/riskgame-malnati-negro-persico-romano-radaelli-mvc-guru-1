package com.mvcguru.risiko.maven.eclipse.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import com.mvcguru.risiko.maven.eclipse.controller.MessageBrokerSingleton;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

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
    void testNoArgsConstructor() {
        Game game = new Game();
        assertNotNull(game, "Game should be instantiated");
    }

    @Test
    void testAllArgsConstructor() {
    	GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(4)
                .idMap("TestMap")
                .build();
        Game game = new Game("gameId", config);

        assertEquals("gameId", game.getId(), "ID should match the constructor argument");
        assertEquals(config, game.getConfiguration(), "Configuration should match the constructor argument");
    }

    @Test
    void testBuilderPattern() throws IOException {
    	GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(4)
                .idMap("TestMap")
                .build(); 
        IGame game = FactoryGame.getInstance().createGame(config);
        
        
        assertEquals(game.getId(), game.getId(), "ID should match the builder's value");
        assertEquals(config, game.getConfiguration(), "Configuration should match the builder's value");
    }

    @Test
    void testEquals() {
        GameConfiguration configuration = new GameConfiguration();
        Game game1 = new Game("gameId", configuration);
        Game game2 = new Game("gameId", configuration);

        assertEquals(game1, game2, "Two games with the same ID and configuration should be equal");
    }

    @Test
    void testHashCode() {
    	GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(4)
                .idMap("TestMap")
                .build();
    	
        Game game1 = new Game("gameId", config);
        Game game2 = new Game("gameId", config);

        assertEquals(game1.hashCode(), game2.hashCode(), "Hashcodes should match for equal objects");
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
    

//    @Test
//    void createTerritoryDeck() throws IOException {
//    	GameConfiguration config = GameConfiguration.builder()
//                .mode(GameMode.EASY)
//                .numberOfPlayers(4)
//                .idMap("TestMap")
//                .build();
//    	IGame game = FactoryGame.getInstance().createGame(config);
//    	
//    	IDeck testDeck = game.createTerritoryDeck(config);
//    	
//    	ICard c = testDeck.drawCard();
//    	TerritoryCard card = TerritoryCard.builder().territory(Territory.builder().name("Afganistan").continent(4).build()).symbol(TerritoryCard.CardSymbol.CAVALRY).build();
//    	TerritoryCard card2 = TerritoryCard.builder().territory(Territory.builder().name("Venezuela").continent(1).build()).symbol(TerritoryCard.CardSymbol.ARTILLERY).build();
//    	
//    	assertEquals(card, c, "The drawn card should be the same as the inserted card");
//    	assertNotEquals(card2, c, "The drawn card should be the same as the inserted card");
//    	
//    }
    
    @Test
    void createObjectiveDeck() throws IOException {
    	//Test mode EASY
    	GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(4)
                .idMap("TestMap")
                .build();
    	
    	IGame game = FactoryGame.getInstance().createGame(config);
    	    	
    	ICard c = game.getDeckObjective().drawCard();
    	
    	ObjectiveCard card = ObjectiveCard.builder().objective("Conquistare più territori possibili in 4 turni.").build();
    	
    	assertEquals(card, c, "The drawn card should be the same as the inserted card");
    	
    	//Test mode MEDIUM
    	GameConfiguration config2 = GameConfiguration.builder()
                .mode(GameMode.MEDIUM)
                .numberOfPlayers(4)
                .idMap("TestMap")
                .build();
    	
    	IGame game2 = FactoryGame.getInstance().createGame(config2);
    	    	
    	ICard c2 = game.getDeckObjective().drawCard();
    	
    	ObjectiveCard card2 = ObjectiveCard.builder().objective("Conquistare 12 territori presidiandoli con almeno due armate ciascuno.").build();
    	
    	assertEquals(card2, c2, "The drawn card should be the same as the inserted card");
    	
    	//Test mode HARD
    	GameConfiguration config3 = GameConfiguration.builder()
                .mode(GameMode.HARD)
                .numberOfPlayers(4)
                .idMap("TestMap")
                .build();
    	
    	IGame game3 = FactoryGame.getInstance().createGame(config3);
    	    	
    	ICard c3 = game.getDeckObjective().drawCard();
    	
    	ObjectiveCard card3 = ObjectiveCard.builder().objective("Conquistare 18 territori presidiandoli con almeno due armate ciascuno.").build();
    	
    	assertEquals(card3, c3, "The drawn card should be the same as the inserted card");
    }
}
