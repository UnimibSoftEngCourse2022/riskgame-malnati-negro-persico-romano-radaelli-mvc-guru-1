package com.mvcguru.risiko.maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;

class UserExceptionTest {

	@Test
	void testUserException() throws DatabaseConnectionException, UserException {
	    DaoSQLiteImpl userDao = new DaoSQLiteImpl("jdbc:sqlite:testdatabase.db");
	    assertThrows(UserException.class, () -> userDao.registerUser(null));
	}



}
