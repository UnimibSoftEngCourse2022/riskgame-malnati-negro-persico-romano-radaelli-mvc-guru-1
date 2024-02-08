package com.mvcguru.risiko.maven.eclipse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoSQLiteImplTest {
    private UserDaoSQLiteImpl userDao;

    @BeforeEach
    void setUp() throws DatabaseConnectionException, UserException {
        userDao = new UserDaoSQLiteImpl("jdbc:sqlite:testdatabase.db");
        userDao.createUsersTable();
    }

    @Test
    void testRegisterUser() throws UserException {
        User user = new User("testUser1", "testPassword");
        userDao.registerUser(user);
        User retrievedUser = userDao.getUserByUsernameAndPassword("testUser1", "testPassword");
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        userDao.deleteUser(user);
    }

    @Test
    void testDeleteUser() throws UserException {
        User user = new User("testUser2", "testPassword");
        userDao.registerUser(user);
        userDao.deleteUser(user);
        User retrievedUser = userDao.getUserByUsernameAndPassword("testUser2", "testPassword");
        assertNull(retrievedUser);
    }
}
