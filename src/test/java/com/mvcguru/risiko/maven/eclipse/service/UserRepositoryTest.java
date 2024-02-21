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
    void testinsertUser() throws UserException, GameException {
        User user = User.builder().username("testUser").password("password123").build();
        doNothing().when(dataDao).insertUser(any(User.class));
        userRepository.insertUser(user);
        verify(dataDao, times(1)).insertUser(user);
    }

    @Test
    void testGetUser() throws UserException {
        User user = User.builder().username("testUser").password("password123").build();
        when(dataDao.getUser(user.getUsername(), user.getPassword()));
        User result = userRepository.getUser(user.getUsername(), user.getPassword());
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void testDeleteUser() throws UserException {
        User user = User.builder().username("testUser").password("password123").build();
        doNothing().when(dataDao).deleteUser(any(User.class));
        userRepository.deleteUser(user);
        verify(dataDao, times(1)).deleteUser(user);
    }
}
