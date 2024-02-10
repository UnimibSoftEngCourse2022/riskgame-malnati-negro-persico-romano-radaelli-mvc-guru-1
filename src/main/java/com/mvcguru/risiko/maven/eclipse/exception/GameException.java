package com.mvcguru.risiko.maven.eclipse.exception;

import java.sql.SQLException;

public class GameException extends Exception{
	public GameException(String message, SQLException e) {
        super(message, e);
    }
}
