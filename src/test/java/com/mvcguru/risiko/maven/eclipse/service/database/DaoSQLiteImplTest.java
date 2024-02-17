package com.mvcguru.risiko.maven.eclipse.service.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

class DaoSQLiteImplTest {
    private DaoSQLiteImpl data;

    @BeforeEach
    void setUp() throws DatabaseConnectionException, UserException, GameException {
        data = DaoSQLiteImpl.getInstance();
    }

    @Test
    void testRegisterUser() throws UserException {
        User user = new User("testUser1", "testPassword");
        data.registerUser(user);
        User retrievedUser = data.getUserByUsernameAndPassword("testUser1", "testPassword");
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        data.deleteUser(user);
    }

    @Test
    void testDeleteUser() throws UserException {
        User user = new User("testUser2", "testPassword");
        data.registerUser(user);
        data.deleteUser(user);
        User retrievedUser = data.getUserByUsernameAndPassword("testUser2", "testPassword");
        assertNull(retrievedUser);
    }

    @Test
    void testRegisterAndGetGameById() throws IOException, GameException {
        GameConfiguration config = GameConfiguration.builder()
                                    .mode(GameMode.EASY)
                                    .numberOfPlayers(4)
                                    .idMap("TestMap")
                                    .build();
        IGame gameToRegister = FactoryGame.getInstance().createGame(config);
        data.registerGame(gameToRegister);
        IGame retrievedGame = data.getGameById(gameToRegister.getId());
        assertNotNull(retrievedGame);
        assertEquals(gameToRegister.getId(), retrievedGame.getId());
        assertEquals(gameToRegister.getConfiguration().getMode(), retrievedGame.getConfiguration().getMode());
        assertEquals(gameToRegister.getConfiguration().getNumberOfPlayers(), retrievedGame.getConfiguration().getNumberOfPlayers());
        assertEquals(gameToRegister.getConfiguration().getIdMap(), retrievedGame.getConfiguration().getIdMap());
        data.deleteGame(gameToRegister);
    }

    @Test
    void testDeleteGame() throws IOException, GameException {
        GameConfiguration config = GameConfiguration.builder()
                                    .mode(GameMode.MEDIUM)
                                    .numberOfPlayers(4)
                                    .idMap("TestMap")
                                    .build();
        IGame gameToRegister = FactoryGame.getInstance().createGame(config);
        data.registerGame(gameToRegister);
        data.deleteGame(gameToRegister);
        IGame retrievedGame = data.getGameById(gameToRegister.getId());
        assertNull(retrievedGame);
    }

    @Test
    void testGetAllGames() throws IOException, GameException {
        GameConfiguration config1 = GameConfiguration.builder()
                                    .mode(GameMode.EASY)
                                    .numberOfPlayers(3)
                                    .idMap("TestMap1")
                                    .build();
        IGame game1 = FactoryGame.getInstance().createGame(config1);
        data.registerGame(game1);
        GameConfiguration config2 = GameConfiguration.builder()
                                    .mode(GameMode.HARD)
                                    .numberOfPlayers(2)
                                    .idMap("TestMap2")
                                    .build();
        IGame game2 = FactoryGame.getInstance().createGame(config2);
        data.registerGame(game2);
        List<IGame> games = data.getAllGames();
        assertNotNull(games);
        assertTrue(games.size() >= 2);
        data.deleteGame(game1);
        data.deleteGame(game2);
    }
    
    @Test
    void testInsertAndDeletePlayer() throws GameException {
        Player testPlayer = Player.builder().userName("testuser").gameId("game1").color(Player.PlayerColor.RED).build();
        data.insertPlayer(testPlayer);
        List<Player> usersInGame = data.getPlayerInGame("game1");
        assertTrue(usersInGame.stream().anyMatch(player -> player.getUserName().equals("testuser")));
        data.deletePlayer("testuser");
        usersInGame = data.getPlayerInGame("game1");
        assertTrue(usersInGame.stream().noneMatch(player -> player.getUserName().equals("testuser")));
    }

    @Test
    void testGetUsersInGame() throws GameException {
        Player player1 = Player.builder().userName("user1").gameId("game2").color(Player.PlayerColor.BLUE).build();
        Player player2 = Player.builder().userName("user2").gameId("game2").color(Player.PlayerColor.RED).build();
        data.insertPlayer(player1);
        data.insertPlayer(player2);
        List<Player> usersInGame = data.getPlayerInGame("game2");
        assertEquals(2, usersInGame.size());
        assertTrue(usersInGame.stream().anyMatch(player -> player.getUserName().equals("user1")));
        assertTrue(usersInGame.stream().anyMatch(player -> player.getUserName().equals("user2")));
        data.deletePlayer("user1");
        data.deletePlayer("user2");
    }
}
