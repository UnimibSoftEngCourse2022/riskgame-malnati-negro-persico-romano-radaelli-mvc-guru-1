package com.mvcguru.risiko.maven.eclipse.exception;

import java.sql.SQLException;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
