package com.mvcguru.risiko.maven.eclipse.states;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

class GameSetupStateTest {

    private GameSetupState gameSetupState;

    @Mock
    private IGame game;
    @Mock
    private IDeck deckTerritory;
    @Mock
    private IDeck deckObjective;
    @Mock
    private TerritoryCard territoryCard;

	/*
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Configura i mock del deck e delle carte
        when(deckTerritory.drawCard()).thenReturn(territoryCard, null); // Assumi che drawCard restituisca una carta e poi null
        when(deckObjective.drawCard()).thenReturn(mock(TerritoryCard.class)); // Assumi che ogni chiamata restituisca una nuova carta
        
        gameSetupState = GameSetupState.builder().game(game).build();
        
        // Configurazione del gioco con i giocatori e i deck
        when(game.getPlayers()).thenReturn((ArrayList<Player>) Arrays.asList(new Player(), new Player()));
        when(game.getDeckTerritory()).thenReturn(deckTerritory);
        when(game.getDeckObjective()).thenReturn(deckObjective);
        
        // Configurazione dei giocatori per simulare l'effetto delle assegnazioni
        game.getPlayers().forEach(player -> {
            player.setTerritories(new ArrayList<>());
        });
    }*/

    /*
    @Test
    void testSetUpGameAssignsTerritoriesAndObjectives() {
    	
        gameSetupState.setUpGame(); // Assumi che questo metodo sia accessibile per il test o che venga chiamato indirettamente
        
        // Verifica che i giocatori abbiano ricevuto territori e obiettivi
        for (Player player : game.getPlayers()) {
            assertFalse(player.getTerritories().isEmpty(), "Player should have territories assigned");
            assertNotNull(player.getObjective(), "Player should have an objective assigned");
        }
        
        // Verifica che i deck siano stati mescolati
        verify(deckTerritory, times(1)).shuffle();
        verify(deckObjective, times(1)).shuffle();
    }*/
	
	@Test
	void testtest() {
		assertTrue(true);
	}
    
    @Test
    void testLobbyToGameSetupTransitionAssignsResourcesCorrectly() throws FullGameException, IOException {
        // Configurazione iniziale del gioco e dello stato di lobby
        //IGame game1 = mock(IGame.class);
        GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(2)
                .idMap("TestMap")
                .build();
        //when(game1.getConfiguration()).thenReturn(config);
        //when(game1.getPlayers()).thenReturn(new ArrayList<>());

        // Mock dei deck e delle carte per simulare l'assegnazione
        /*IDeck deckTerritory = mock(IDeck.class);
        IDeck deckObjective = mock(IDeck.class);
        when(game1.getDeckTerritory()).thenReturn(deckTerritory);
        when(game1.getDeckObjective()).thenReturn(deckObjective);
        when(deckTerritory.drawCard()).thenReturn(new TerritoryCard(), null); // Assumi almeno una carta disponibile
        when(deckObjective.drawCard()).thenReturn(new ObjectiveCard()); // Assumi una carta obiettivo disponibile*/

        
        IGame game = FactoryGame.getInstance().createGame(config);
        game.setState(LobbyState.builder().game(game).build());


        GameEntry gameEntry1 = GameEntry.builder().player(Player.builder().userName("player1").build()).build();
        game.getState().onActionPlayer(gameEntry1);
        
        GameEntry gameEntry2 = GameEntry.builder().player(Player.builder().userName("player2").build()).build();
        game.getState().onActionPlayer(gameEntry2);

        verify(game).setState(any(GameSetupState.class));

        
        for (Player player : game.getPlayers()) {
            assertFalse(player.getTerritories().isEmpty(), "Each player should have territories assigned");
            System.out.println(player.getTerritories());
            
            assertNotNull(player.getObjective(), "Each player should have an objective assigned");
            System.out.println(player.getObjective());
            
            assertNotNull(player.getColor(), "Each player should have a color assigned");
            System.out.println(player.getColor());
            
        }
    }

}

