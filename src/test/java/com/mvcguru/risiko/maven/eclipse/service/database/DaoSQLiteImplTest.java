package com.mvcguru.risiko.maven.eclipse.service.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

class DaoSQLiteImplTest {
	Logger LOGGER = LoggerFactory.getLogger(DaoSQLiteImplTest.class);
    private DaoSQLiteImpl data;

    @BeforeEach
    void setUp() throws DatabaseConnectionException, UserException, GameException {
        data = DaoSQLiteImpl.getInstance();
    }

    @Test
    void testinsertUser() throws UserException, GameException {
        User user = new User("testUser1", "testPassword");
        data.insertUser(user);
        User retrievedUser = data.getUserByUsernameAndPassword("testUser1", "testPassword");
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        data.deleteUser(user);
    }

    @Test
    void testDeleteUser() throws UserException, GameException {
        User user = new User("testUser2", "testPassword");
        data.insertUser(user);
        data.deleteUser(user);
        User retrievedUser = data.getUserByUsernameAndPassword("testUser2", "testPassword");
        assertNull(retrievedUser);
    }

    @Test
    void testinsertAndGetGameById() throws IOException, GameException {
        GameConfiguration config = GameConfiguration.builder()
                                    .mode(GameMode.EASY)
                                    .numberOfPlayers(4)
                                    .idMap("TestMap")
                                    .build();
        IGame gameToinsert = FactoryGame.getInstance().createGame(config);
        data.insertGame(gameToinsert);
        IGame retrievedGame = data.getGameById(gameToinsert.getId());
        assertNotNull(retrievedGame);
        assertEquals(gameToinsert.getId(), retrievedGame.getId());
        assertEquals(gameToinsert.getConfiguration().getMode(), retrievedGame.getConfiguration().getMode());
        assertEquals(gameToinsert.getConfiguration().getNumberOfPlayers(), retrievedGame.getConfiguration().getNumberOfPlayers());
        assertEquals(gameToinsert.getConfiguration().getIdMap(), retrievedGame.getConfiguration().getIdMap());
        data.deleteGame(gameToinsert);
    }

    @Test
    void testDeleteGame() throws IOException, GameException {
        GameConfiguration config = GameConfiguration.builder()
                                    .mode(GameMode.MEDIUM)
                                    .numberOfPlayers(4)
                                    .idMap("TestMap")
                                    .build();
        IGame gameToinsert = FactoryGame.getInstance().createGame(config);
        data.insertGame(gameToinsert);
        data.deleteGame(gameToinsert);
        IGame retrievedGame = data.getGameById(gameToinsert.getId());
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
        data.insertGame(game1);
        GameConfiguration config2 = GameConfiguration.builder()
                                    .mode(GameMode.HARD)
                                    .numberOfPlayers(2)
                                    .idMap("TestMap2")
                                    .build();
        IGame game2 = FactoryGame.getInstance().createGame(config2);
        data.insertGame(game2);
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
    
    @Test
	void testGetConnection() {
		try {
			assertNotNull(data.getConnection("jdbc:sqlite:mydatabase.db"));
		} catch (SQLException e) {
			LOGGER.error("Errore durante la connessione al database", e);
		}
	}
    
    @Test
	void insertTurn() throws GameException {
    	Player player = Player.builder().userName("user1").gameId("game1").color(Player.PlayerColor.RED).build();
    	Turn turn = Turn.builder().indexTurn(1).player(player).isConquered(false).build();
		data.insertTurn(turn);
		assertTrue(data.getLastTurnByGameId(player.getGameId()) != null);
		data.deleteTurn(turn);
	}
    
    @Test
	void insertComboCards() throws GameException {
		Player player = Player.builder().userName("user1").gameId("game1").color(Player.PlayerColor.RED).build();
		Turn turn = Turn.builder().indexTurn(1).player(player).build();
		Territory territory = Territory.builder().name("territory1").build();
		TerritoryCard card1 = TerritoryCard.builder().territory(territory).symbol(CardSymbol.ARTILLERY).build();
		
		data.insertComboCard(card1, player, player.getGameId());
		List<TerritoryCard> cards = data.getAllComboCards(player.getUserName(), player.getGameId());
		
		assertTrue(cards.contains(card1));
		
		data.deleteComboCard(card1, player, player.getGameId());
		data.deleteTurn(turn);
		
	}
    
    @Test
	void updateOwner() throws GameException {
		Player player = Player.builder().userName("user1").gameId("game1").color(Player.PlayerColor.RED).build();
		TerritoryCard t = TerritoryCard.builder().territory(Territory.builder().name("territory1").build()).symbol(CardSymbol.ARTILLERY).build();
		data.updateOwner(t, player.getUserName(), player.getGameId());
        
		List<TerritoryCard> cards = data.getAllComboCards(player.getUserName(), player.getGameId());
		
		assertTrue(!cards.contains(t));
		
		data.deleteComboCard(t, player, player.getGameId());
		
		assertTrue(!cards.contains(t));
		
    }
    
    @Test
    void updateIsConquered() throws GameException {
    	Player playerInstance = Player.builder().userName("user1").gameId("game1").build();
    	Territory attackerTerritoryInstance = null;
    	Territory defenderTerritoryInstance = null;
    	Turn turn = Turn.builder()
                .player(playerInstance) 
                .numberOfTroops(10) 
                .indexTurn(1) 
                .attackerTerritory(attackerTerritoryInstance) 
                .defenderTerritory(defenderTerritoryInstance) 
                .numAttDice(3)
                .numDefDice(2) 
                .isConquered(false)
                .build();
    	
    	LOGGER.info("Turn: " + turn);
    	
    	data.insertTurn(turn);

    	assertTrue(data.getTurnByIndex(turn.getPlayer().getGameId(), turn.getPlayer().getUserName(), 1).getIndexTurn() == 1);

    	data.updateIsConquered(turn, true);
    	LOGGER.info("Turn: " + data.getTurnByIndex(turn.getPlayer().getGameId(), turn.getPlayer().getUserName(), 1));
    	
    	assertTrue(data.getTurnByIndex(turn.getPlayer().getGameId(), turn.getPlayer().getUserName(), turn.getIndexTurn()).isConquered() == true);
    	
    	data.deleteTurn(turn);
    	
    	assertTrue(data.getTurnByIndex(turn.getPlayer().getGameId(), turn.getPlayer().getUserName(), turn.getIndexTurn()) == null);
    }
    
    
}
