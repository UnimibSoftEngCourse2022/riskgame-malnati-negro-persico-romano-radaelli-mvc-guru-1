package com.mvcguru.risiko.maven.eclipse.service;

import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.service.database.DataDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTest {

    private UserRepository userRepository;
    private DataDao mockedDataDao;

    @BeforeEach
    void setUp() throws Exception {
        userRepository = UserRepository.getInstance(); // Assuming getInstance handles exceptions internally for simplicity
        mockedDataDao = mock(DataDao.class);
        
        // Injecting mocked DataDao instance using reflection
        Field dbField = UserRepository.class.getDeclaredField("db");
        dbField.setAccessible(true);
        dbField.set(userRepository, mockedDataDao);
    }

    @Test
    void testRegisterUser() throws UserException {
        User user = new User("testUser", "password");
        userRepository.registerUser(user);
        verify(mockedDataDao, times(1)).registerUser(user);
    }

    @Test
    void testGetUser() throws UserException {
        User expectedUser = new User("testUser", "password");
        when(mockedDataDao.getUserByUsernameAndPassword("testUser", "password")).thenReturn(expectedUser);
        
        User actualUser = userRepository.getUser(expectedUser);
        
        verify(mockedDataDao, times(1)).getUserByUsernameAndPassword("testUser", "password");
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testGetUserName() throws UserException {
        User user = new User("testUser", "password");
        when(mockedDataDao.getUserByUsernameAndPassword("testUser", "password")).thenReturn(user);
        
        String userName = userRepository.getUserName(user);
        
        verify(mockedDataDao, times(1)).getUserByUsernameAndPassword("testUser", "password");
        assertEquals("testUser", userName);
    }

    @Test
    void testGetUserPassword() throws UserException {
        User user = new User("testUser", "password");
        when(mockedDataDao.getUserByUsernameAndPassword("testUser", "password")).thenReturn(user);
        
        String userPassword = userRepository.getUserPassword(user);
        
        verify(mockedDataDao, times(1)).getUserByUsernameAndPassword("testUser", "password");
        assertEquals("password", userPassword);
    }
}
