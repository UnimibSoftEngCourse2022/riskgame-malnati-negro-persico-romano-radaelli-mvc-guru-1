package com.mvcguru.risiko.maven.eclipse.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.UserDao;
import com.mvcguru.risiko.maven.eclipse.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;

class UserRepositoryTest {

    private UserRepository userRepository;
    private UserDao mockedUserDao;

    @BeforeEach
    void setUp() throws Exception {
        mockedUserDao = mock(UserDao.class);
        userRepository = new UserRepository();
        java.lang.reflect.Field field = UserRepository.class.getDeclaredField("db");
        field.setAccessible(true);
        field.set(userRepository, mockedUserDao);
    }

    @Test
    void testRegisterUser() throws UserException {
        User user = new User("testUser", "password");
        userRepository.registerUser(user);
        verify(mockedUserDao, times(1)).registerUser(user);
    }

    @Test
    void testGetUser() throws UserException {
        User user = new User("testUser", "password");
        when(mockedUserDao.getUserByUsernameAndPassword("testUser", "password")).thenReturn(user);
        User retrievedUser = userRepository.getUser(user);
        verify(mockedUserDao, times(1)).getUserByUsernameAndPassword("testUser", "password");
        assertEquals(user, retrievedUser);
    }
}
