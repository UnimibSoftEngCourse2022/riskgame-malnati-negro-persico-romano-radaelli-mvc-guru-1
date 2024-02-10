package com.mvcguru.risiko.maven.eclipse.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.service.database.DaoSQLiteImpl;

class DatabaseConnectionExceptionTest {

	@Test
	void testDatabaseConnectionException(){
	    assertThrows(DatabaseConnectionException.class, () -> {
	        new DaoSQLiteImpl(null);
	    });
	}


}
