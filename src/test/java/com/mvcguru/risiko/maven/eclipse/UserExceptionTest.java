package com.mvcguru.risiko.maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.exception.UserException;

class UserExceptionTest {

	@Test
	void testUserException() {
	    assertThrows(UserException.class, () -> {
	        UserDaoSQLiteImpl userDao = new UserDaoSQLiteImpl("jdbc:sqlite:testdatabase.db");
	        userDao.registerUser(null);
	    });
	}


}
