package com.mvcguru.risiko.maven.eclipse.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.database.DataDao;

import java.util.ArrayList;
import java.util.List;

class GameRepositoryTest {

    @Mock
    private DataDao dataDao;

    @InjectMocks
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterGame() throws GameException {
        IGame game = mock(IGame.class);
        doNothing().when(dataDao).registerGame(any(IGame.class));
        gameRepository.registerGame(game);
        verify(dataDao, times(1)).registerGame(game);
    }

    @Test
    void testDeleteGame() throws GameException {
        IGame game = mock(IGame.class);
        doNothing().when(dataDao).deleteGame(any(IGame.class));
        gameRepository.deleteGame(game);
        verify(dataDao, times(1)).deleteGame(game);
    }

    @Test
    void testGetGameById() throws GameException, FullGameException {
        String gameId = "testGameId";
        IGame expectedGame = mock(IGame.class);
        when(dataDao.getGameById(gameId)).thenReturn(expectedGame);
        when(dataDao.getPlayerInGame(gameId)).thenReturn(new ArrayList<>());
        IGame actualGame = gameRepository.getGameById(gameId);
        assertEquals(expectedGame, actualGame);
        verify(dataDao, times(1)).getGameById(gameId);
    }

    @Test
    void testGetAllGames() throws GameException {
        List<IGame> expectedGames = new ArrayList<>();
        when(dataDao.getAllGames()).thenReturn(expectedGames);
        List<IGame> actualGames = gameRepository.getAllGames();
        assertEquals(expectedGames, actualGames);
        verify(dataDao, times(1)).getAllGames();
    }

    @Test
    void testAddPlayer() throws GameException {
        Player player = new Player();
        doNothing().when(dataDao).insertPlayer(any(Player.class));
        gameRepository.add(player);
        verify(dataDao, times(1)).insertPlayer(player);
    }

    @Test
    void testRemovePlayer() throws GameException {
        String username = "testUser";
        doNothing().when(dataDao).deletePlayer(username);
        gameRepository.remove(username);
        verify(dataDao, times(1)).deletePlayer(username);
    }
}
