package com.mvcguru.risiko.maven.eclipse;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDaoSQLiteImplTest {
    private UserDao userDao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws DatabaseConnectionException, UserException {
        userDao = new UserDaoSQLiteImpl("jdbc:sqlite:testdatabase.db");
        userDao.createUsersTable();
        connection = DatabaseConnection.getConnection();
    }

    @Test
    public void testGetUserByUsernameAndPassword() throws UserException {
        String testUsername = "Bobby";
        String testPassword = "bobby00";
        User testUser = new User(testUsername, testPassword);
        
        String testUsername1 = "Dani";
        String testPassword1 = "dani00";
        User testUser1 = new User(testUsername1, testPassword1);
        
        String testUsername2 = "Andre";
        String testPassword2 = "andre00";
        User testUser2 = new User(testUsername2, testPassword2);
        
        String testUsername3 = "Lore";
        String testPassword3 = "lore00";
        User testUser3 = new User(testUsername3, testPassword3);
        
        String testUsername4 = "Perse";
        String testPassword4 = "perse00";
        User testUser4 = new User(testUsername4, testPassword4);
     
        userDao.registerUser(testUser);
        userDao.registerUser(testUser1);
        userDao.registerUser(testUser2);
        userDao.registerUser(testUser3);
        userDao.registerUser(testUser4);

        User retrievedUser = userDao.getUserByUsernameAndPassword(testUsername, testPassword);
        User retrievedUser1 = userDao.getUserByUsernameAndPassword(testUsername1, testPassword1);
        User retrievedUser2 = userDao.getUserByUsernameAndPassword(testUsername2, testPassword2);
        User retrievedUser3 = userDao.getUserByUsernameAndPassword(testUsername3, testPassword3);
        User retrievedUser4 = userDao.getUserByUsernameAndPassword(testUsername4, testPassword4);
        
        assertNotNull(retrievedUser, "L'utente recuperato non dovrebbe essere null");
        assertEquals(testUsername, retrievedUser.getUsername(), "I nomi utente dovrebbero corrispondere");
        assertEquals(testPassword, retrievedUser.getPassword(), "Le password dovrebbero corrispondere");
        
        assertNotNull(retrievedUser1, "L'utente recuperato non dovrebbe essere null");
        assertEquals(testUsername1, retrievedUser1.getUsername(), "I nomi utente dovrebbero corrispondere");
        assertEquals(testPassword1, retrievedUser1.getPassword(), "Le password dovrebbero corrispondere");
        
        assertNotNull(retrievedUser2, "L'utente recuperato non dovrebbe essere null");
        assertEquals(testUsername2, retrievedUser2.getUsername(), "I nomi utente dovrebbero corrispondere");
        assertEquals(testPassword2, retrievedUser2.getPassword(), "Le password dovrebbero corrispondere");
        
        assertNotNull(retrievedUser3, "L'utente recuperato non dovrebbe essere null");
        assertEquals(testUsername3, retrievedUser3.getUsername(), "I nomi utente dovrebbero corrispondere");
        assertEquals(testPassword3, retrievedUser3.getPassword(), "Le password dovrebbero corrispondere");
        
        assertNotNull(retrievedUser4, "L'utente recuperato non dovrebbe essere null");
        assertEquals(testUsername4, retrievedUser4.getUsername(), "I nomi utente dovrebbero corrispondere");
        assertEquals(testPassword4, retrievedUser4.getPassword(), "Le password dovrebbero corrispondere");

        userDao.deleteUser(testUser);
        userDao.deleteUser(testUser1);
        userDao.deleteUser(testUser2);
        userDao.deleteUser(testUser3);
        userDao.deleteUser(testUser4);
    }
    
    @Test
    public void testGetNonExistentUser() throws UserException {
        String nonExistentUsername = "nonExistentUser";
        String nonExistentPassword = "nonExistentPassword";

        User retrievedUser = userDao.getUserByUsernameAndPassword(nonExistentUsername, nonExistentPassword);
        assertNull(retrievedUser, "L'utente recuperato dovrebbe essere null perché non esiste nel database");
    }


    @AfterEach
    public void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
