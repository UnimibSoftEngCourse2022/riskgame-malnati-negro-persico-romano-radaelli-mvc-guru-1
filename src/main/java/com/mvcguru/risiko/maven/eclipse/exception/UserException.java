package com.mvcguru.risiko.maven.eclipse.exception;

import java.sql.SQLException;

public class UserException extends Exception {
	public UserException(String message, SQLException e) {
        super(message, e);
    }
}
