package com.mvcguru.risiko.maven.eclipse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

class UserDaoSQLiteImplTest {
    private UserDaoSQLiteImpl userDao;

    @BeforeEach
    void setUp() throws DatabaseConnectionException, UserException {
        userDao = new UserDaoSQLiteImpl("jdbc:sqlite:testdatabase.db");
        userDao.createUsersTable();
    }

    @Test
    void testRegisterUser() throws UserException, SQLException {
        User user = new User("testUser1", "testPassword");
        userDao.registerUser(user);
        User retrievedUser = userDao.getUserByUsernameAndPassword("testUser1", "testPassword");
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        userDao.deleteUser(user);
    }

    @Test
    void testDeleteUser() throws UserException, SQLException {
        User user = new User("testUser2", "testPassword");
        userDao.registerUser(user);
        userDao.deleteUser(user);
        User retrievedUser = userDao.getUserByUsernameAndPassword("testUser2", "testPassword");
        assertNull(retrievedUser);
    }
    
    @Test
    void testCloseConnection() throws UserException {
        userDao.closeConnection();
        try {
            assertTrue(userDao.getConnection().isClosed());
        } catch (SQLException e) {
            fail("Errore durante la verifica della chiusura della connessione.", e);
        }
    }
}
