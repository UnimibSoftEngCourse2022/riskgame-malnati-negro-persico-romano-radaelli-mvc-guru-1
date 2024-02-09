package com.mvcguru.risiko.maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DatabaseConnectionExceptionTest {

	@Test
	void testDatabaseConnectionException(){
	    assertThrows(DatabaseConnectionException.class, () -> {
	        new UserDaoSQLiteImpl(null);
	    });
	}


}
