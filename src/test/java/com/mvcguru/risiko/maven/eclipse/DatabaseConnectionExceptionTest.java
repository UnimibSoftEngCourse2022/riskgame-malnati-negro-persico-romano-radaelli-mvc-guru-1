package com.mvcguru.risiko.maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;

class DatabaseConnectionExceptionTest {

	@Test
	void testDatabaseConnectionException(){
	    assertThrows(DatabaseConnectionException.class, () -> {
	        new UserDaoSQLiteImpl(null);
	    });
	}


}
