package com.mvcguru.risiko.maven.eclipse.service.database;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.service.database.DaoSQLiteImpl;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

class DaoSQLiteImplTest {
    private DaoSQLiteImpl userDao;

    @BeforeEach
    void setUp() throws DatabaseConnectionException, UserException {
        userDao = new DaoSQLiteImpl("jdbc:sqlite:testdatabase.db");
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
}
