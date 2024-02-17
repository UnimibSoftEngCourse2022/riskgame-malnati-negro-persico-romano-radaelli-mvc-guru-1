package com.mvcguru.risiko.maven.eclipse.service.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

class DaoSQLiteImplTest {
    private DataDao data;
    

    @BeforeEach
    void setUp() {
        try {
            data = DaoSQLiteImpl.getInstance();
        } catch (Exception e) {
            fail("Error during test setup: " + e.getMessage(), e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            data.closeConnection(); 
        } catch (Exception e) {
            fail("Error during database connection closure: " + e.getMessage(), e);
        }
    }

    @Test
    void testRegisterUser() {
        try {
            User user = new User("testUser1", "testPassword");
            data.registerUser(user);
            User retrievedUser = data.getUserByUsernameAndPassword("testUser1", "testPassword");
            assertEquals(user.getUsername(), retrievedUser.getUsername());
            assertEquals(user.getPassword(), retrievedUser.getPassword());
            data.deleteUser(user);
        } catch (UserException e) {
            fail("Error during user registration test: " + e.getMessage(), e);
        }
    }

    @Test
    void testDeleteUser() {
        try {
            User user = new User("testUser2", "testPassword");
            data.registerUser(user);
            data.deleteUser(user);
            User retrievedUser = data.getUserByUsernameAndPassword("testUser2", "testPassword");
            assertNull(retrievedUser);
        } catch (UserException e) {
            fail("Error during user deletion test: " + e.getMessage(), e);
        }
    }

    @Test
    void testRegisterAndGetGameById() throws IOException {
        try {
            GameConfiguration config = GameConfiguration.builder()
                                        .mode(GameMode.EASY)
                                        .numberOfPlayers(4)
                                        .idMap("TestMap")
                                        .build();
            IGame gameToRegister = FactoryGame.getInstance().createGame(config);
            data.registerGame(gameToRegister);
            
            IGame retrievedGame = data.getGameById(gameToRegister.getId());
            assertNotNull(retrievedGame, "Registered game not found");
            assertEquals(gameToRegister.getId(), retrievedGame.getId(), "Game ID does not match");
            assertEquals(gameToRegister.getConfiguration().getMode().name(), retrievedGame.getConfiguration().getMode().name(), "Game mode does not match");
            assertEquals(gameToRegister.getConfiguration().getNumberOfPlayers(), retrievedGame.getConfiguration().getNumberOfPlayers(), "Number of players does not match");
            assertEquals(gameToRegister.getConfiguration().getIdMap(), retrievedGame.getConfiguration().getIdMap(), "Map ID does not match");
            data.deleteGame(gameToRegister);
        } catch (GameException e) {
            fail("Error during game registration and retrieval test: " + e.getMessage(), e);
        }
    }

    @Test
    void testDeleteGame() throws IOException {
        try {
            GameConfiguration config = GameConfiguration.builder()
                                        .mode(GameMode.MEDIUM)
                                        .numberOfPlayers(4)
                                        .idMap("TestMap")
                                        .build();
            IGame gameToRegister = FactoryGame.getInstance().createGame(config);
            data.registerGame(gameToRegister);

            data.deleteGame(gameToRegister);

            IGame retrievedGame = data.getGameById(gameToRegister.getId());
            assertNull(retrievedGame, "Game was not deleted correctly");
        } catch (GameException e) {
            fail("Error during game deletion test: " + e.getMessage(), e);
        }
    }

    @Test
    void testGetAllGames() throws IOException {
        try {
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
            assertNotNull(games, "Game list is null");
            assertEquals(2, games.size(), "Incorrect number of games in the list");
            assertNotNull(data.getGameById(game1.getId()), "Game 1 is not present in the list");
            assertNotNull(data.getGameById(game2.getId()), "Game 2 is not present in the list");

            data.deleteGame(game1);
            data.deleteGame(game2);
        } catch (GameException e) {
            fail("Error during retrieval of all games test: " + e.getMessage(), e);
        }
    }
    
    @Test
    void testInsertAndDeletePlayer() throws GameException {
    	Player testPlayer = Player.builder().userName("testuser").gameId("game1").territories(new ArrayList<Territory>()).color(Player.PlayerColor.valueOf("RED")).build();
    	data.insertPlayer(testPlayer);
        
        List<Player> usersInGame = data.getPlayerInGame("game1");
        assertEquals(1, usersInGame.size());
        assertTrue(usersInGame.contains(testPlayer));
        
        data.deletePlayer(testPlayer.getUserName());

        usersInGame = data.getPlayerInGame("game1");
        assertEquals(0, usersInGame.size());
    }

    @Test
    void testGetUsersInGame() throws GameException {
    	Player player1 = Player.builder().userName("user1").gameId("game2").territories(new ArrayList<Territory>()).color(Player.PlayerColor.valueOf("BLUE")).build();
    	Player player2 = Player.builder().userName("user2").gameId("game2").territories(new ArrayList<Territory>()).color(Player.PlayerColor.valueOf("RED")).build();
    	data.insertPlayer(player1);
    	data.insertPlayer(player2);

        List<Player> usersInGame = data.getPlayerInGame("game2");
        
        assertEquals(2, usersInGame.size());
        assertEquals(player1, usersInGame.get(0));
        assertEquals(player2, usersInGame.get(1));
    }
    
    
}
