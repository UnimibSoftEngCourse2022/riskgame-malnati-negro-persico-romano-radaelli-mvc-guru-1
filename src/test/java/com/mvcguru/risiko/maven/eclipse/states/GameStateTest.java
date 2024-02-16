package com.mvcguru.risiko.maven.eclipse.states;

import static org.mockito.Mockito.*;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameState = spy(GameState.class);
        doNothing().when(gameState).setUpGame();
        doNothing().when(gameState).playTurn();
        gameState.game = game;
    }

    @Test
    void testSetupGame() {
        gameState.setUpGame();
        verify(gameState, times(1)).setUpGame();
    }

    @Test
    void testPlayTurn() {
        gameState.playTurn();
        verify(gameState, times(1)).playTurn();
    }

    @Test
    void testOnActionPlayerGameEntry() throws FullGameException {
        GameEntry gameEntry = mock(GameEntry.class);
        gameState.onActionPlayer(gameEntry);
        verify(gameState, times(1)).onActionPlayer(gameEntry);
    }

    @Test
    void testOnActionPlayerTerritorySetup() {
        TerritorySetup territorySetup = mock(TerritorySetup.class);
        gameState.onActionPlayer(territorySetup);
        verify(gameState, times(1)).onActionPlayer(territorySetup);
    }
}
