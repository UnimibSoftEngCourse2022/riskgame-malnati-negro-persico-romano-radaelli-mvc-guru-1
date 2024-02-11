package com.mvcguru.risiko.maven.eclipse.service.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

class DaoSQLiteImplTest {
    private DaoSQLiteImpl userDao;
    private DaoSQLiteImpl gamesDao;

    @BeforeEach
    void setUp() {
        try {
            userDao = new DaoSQLiteImpl("jdbc:sqlite:testdatabase_user.db");
            gamesDao = new DaoSQLiteImpl("jdbc:sqlite:testdatabase_games.db");
        } catch (Exception e) {
            fail("Errore durante la configurazione dei test", e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            userDao.closeConnection();
            gamesDao.closeConnection();
        } catch (Exception e) {
            fail("Errore durante la chiusura della connessione al database", e);
        }
    }

    // Test per userDao
    @Test
    void testRegisterUser() {
        try {
            User user = new User("testUser1", "testPassword");
            userDao.registerUser(user);
            User retrievedUser = userDao.getUserByUsernameAndPassword("testUser1", "testPassword");
            assertEquals(user.getUsername(), retrievedUser.getUsername());
            assertEquals(user.getPassword(), retrievedUser.getPassword());
            userDao.deleteUser(user);
        } catch (UserException e) {
            fail("Errore durante il test di registrazione utente", e);
        }
    }

    @Test
    void testDeleteUser() {
        try {
            User user = new User("testUser2", "testPassword");
            userDao.registerUser(user);
            userDao.deleteUser(user);
            User retrievedUser = userDao.getUserByUsernameAndPassword("testUser2", "testPassword");
            assertNull(retrievedUser);
        } catch (UserException e) {
            fail("Errore durante il test di eliminazione utente", e);
        }
    }

    // Test per gamesDao
    @Test
    void testRegisterAndGetGameById() {
        try {
            GameConfiguration config = GameConfiguration.builder()
                                        .mode(GameMode.EASY)
                                        .numberOfPlayers(4)
                                        .idMap("TestMap")
                                        .build();
            IGame gameToRegister = FactoryGame.getInstance().creaPartita(config);
            gamesDao.registerGame(gameToRegister);
            
            IGame retrievedGame = gamesDao.getGameById(gameToRegister.getId());
            assertNotNull(retrievedGame, "Il gioco registrato non è stato trovato");
            assertEquals(gameToRegister.getId(), 1, "ID del gioco non corrispondente");
            assertEquals(gameToRegister.getConfiguration().getModeString(), retrievedGame.getConfiguration().getModeString(), "Modalità del gioco non corrispondente");
            assertEquals(gameToRegister.getConfiguration().getNumberOfPlayers(), retrievedGame.getConfiguration().getNumberOfPlayers(), "Numero di giocatori non corrispondente");
            assertEquals(gameToRegister.getConfiguration().getIdMap(), retrievedGame.getConfiguration().getIdMap(), "ID della mappa non corrispondente");
            gamesDao.deleteGame(gameToRegister);
        } catch (GameException e) {
            fail("Errore durante il test di registrazione e recupero del gioco", e);
        }
    }

    @Test
    void testDeleteGame() {
        try {
            GameConfiguration config = GameConfiguration.builder()
                                        .mode(GameMode.MEDIUM)
                                        .numberOfPlayers(4)
                                        .idMap("TestMap")
                                        .build();
            IGame gameToRegister = FactoryGame.getInstance().creaPartita(config);
            gamesDao.registerGame(gameToRegister);

            gamesDao.deleteGame(gameToRegister);

            IGame retrievedGame = gamesDao.getGameById(gameToRegister.getId());
            assertNull(retrievedGame, "Il gioco non è stato eliminato correttamente");
        } catch (GameException e) {
            fail("Errore durante il test di eliminazione gioco", e);
        }
    }

    @Test
    void testGetAllGames() {
        try {
            GameConfiguration config1 = GameConfiguration.builder()
                                        .mode(GameMode.EASY)
                                        .numberOfPlayers(3)
                                        .idMap("TestMap1")
                                        .build();
            IGame game1 = FactoryGame.getInstance().creaPartita(config1);
            gamesDao.registerGame(game1);

            GameConfiguration config2 = GameConfiguration.builder()
                                        .mode(GameMode.HARD)
                                        .numberOfPlayers(2)
                                        .idMap("TestMap2")
                                        .build();
            IGame game2 = FactoryGame.getInstance().creaPartita(config2);
            gamesDao.registerGame(game2);

            List<IGame> games = gamesDao.getAllGames();
            assertNotNull(games, "La lista dei giochi è nulla");
            assertEquals(2, games.size(), "Il numero di giochi nella lista non è corretto");
            assertNotNull(gamesDao.getGameById(game1.getId()), "Il gioco 1 non è presente nella lista");
            assertNotNull(gamesDao.getGameById(game2.getId()), "Il gioco 2 non è presente nella lista");

            gamesDao.deleteGame(game1);
            gamesDao.deleteGame(game2);
        } catch (GameException e) {
            fail("Errore durante il test di recupero di tutti i giochi", e);
        }
    }

}