package com.mvcguru.risiko.maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserExceptionTest {

	@Test
	void testUserException() throws DatabaseConnectionException, UserException {
	    UserDaoSQLiteImpl userDao = new UserDaoSQLiteImpl("jdbc:sqlite:testdatabase.db");
	    assertThrows(UserException.class, () -> userDao.registerUser(null));
	}



}
