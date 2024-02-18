package com.mvcguru.risiko.maven.eclipse.states;

import static org.mockito.Mockito.*;

import java.io.IOException;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GameStateTest {

    @Mock
    private IGame game;

    private GameState gameState;

    @BeforeEach
    void setUp() throws GameException, DatabaseConnectionException, UserException {
        MockitoAnnotations.openMocks(this);
        gameState = spy(GameState.class);
        doNothing().when(gameState).setUpGame();
        doNothing().when(gameState).playTurn();
        gameState.game = game;
    }
    
    private static class MockGameState extends GameState {
        // Mock subclass to test serialization and implemented methods of GameState.
    }

    @Test
    void testSetupGame() throws GameException, DatabaseConnectionException, UserException {
        gameState.setUpGame();
        verify(gameState, times(1)).setUpGame();
    }

    @Test
    void testPlayTurn() {
        gameState.playTurn();
        verify(gameState, times(1)).playTurn();
    }

    @Test
    void testOnActionPlayerGameEntry() throws FullGameException, GameException, DatabaseConnectionException, UserException {
        GameEntry gameEntry = mock(GameEntry.class);
        gameState.onActionPlayer(gameEntry);
        verify(gameState, times(1)).onActionPlayer(gameEntry);
    }

    @Test
    void testOnActionPlayerTerritorySetup() throws GameException, DatabaseConnectionException, UserException, FullGameException, IOException {
        TerritorySetup territorySetup = mock(TerritorySetup.class);
        gameState.onActionPlayer(territorySetup);
        verify(gameState, times(1)).onActionPlayer(territorySetup);
    }
}
