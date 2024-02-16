package com.mvcguru.risiko.maven.eclipse.states;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

class LobbyStateTest {

    private LobbyState lobbyState;

    @Mock
    private Game game;

    @BeforeEach
    void setUp() throws FullGameException {
        MockitoAnnotations.openMocks(this);
        
        lobbyState = LobbyState.builder().game(game).build();
        GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(2)
                .idMap("TestMap")
                .build();
        
        when(game.getConfiguration()).thenReturn(config);
        
        ArrayList<Player> players = new ArrayList<>();
        when(game.getPlayers()).thenReturn(players);
        doAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            players.add(player);
            return null;
        }).when(game).addPlayer(any(Player.class));

        doAnswer(invocation -> {
            GameState newState = invocation.getArgument(0);
            when(game.getState()).thenReturn(newState);
            return null;
        }).when(game).setState(any(GameState.class));
    }

    @Test
    void testOnActionPlayerDoesNotTransitionBeforeMaxPlayers() throws FullGameException {
        GameEntry gameEntry = GameEntry.builder().player(Player.builder().userName("player1").territories(new ArrayList<Territory>()).build()).build();
        lobbyState.onActionPlayer(gameEntry);
        
        assertEquals(1, game.getPlayers().size());
        verify(game, never()).setState(any(GameSetupState.class));
    }

}
