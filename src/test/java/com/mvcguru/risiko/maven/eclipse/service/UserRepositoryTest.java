package com.mvcguru.risiko.maven.eclipse.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.service.database.DataDao;

class UserRepositoryTest {

    @Mock
    private DataDao dataDao;

    @InjectMocks
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
	void getIstance() throws UserException, DatabaseConnectionException, GameException {
		UserRepository userRepository = UserRepository.getInstance();
		assertNotNull(userRepository);
	}

    @Test
    void testRegisterUser() throws UserException {
        User user = new User("testUser", "password123");
        doNothing().when(dataDao).registerUser(any(User.class));
        userRepository.registerUser(user);
        verify(dataDao, times(1)).registerUser(user);
    }

    @Test
    void testGetUser() throws UserException {
        User user = new User("testUser", "password123");
        when(dataDao.getUserByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
        User result = userRepository.getUser(user);
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void testGetUserName() throws UserException {
        User user = new User("testUser", "password123");
        when(dataDao.getUserByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
        String username = userRepository.getUserName(user);
        assertNotNull(username);
        assertEquals(user.getUsername(), username);
    }

    @Test
    void testGetUserPassword() throws UserException {
        User user = new User("testUser", "password123");
        when(dataDao.getUserByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
        String password = userRepository.getUserPassword(user);
        assertNotNull(password);
        assertEquals(user.getPassword(), password);
    }

    @Test
    void testDeleteUser() throws UserException {
        User user = new User("testUser", "password123");
        doNothing().when(dataDao).deleteUser(any(User.class));
        userRepository.deleteUser(user);
        verify(dataDao, times(1)).deleteUser(user);
    }
}
