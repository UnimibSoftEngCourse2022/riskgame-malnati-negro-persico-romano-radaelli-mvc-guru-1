package com.mvcguru.risiko.maven.eclipse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String SQLITE_DB_URL = "jdbc:sqlite:mydatabase.db";
    
    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(SQLITE_JDBC_DRIVER);
            connection = DriverManager.getConnection(SQLITE_DB_URL);
        } catch (ClassNotFoundException e) {LOGGER.error("Driver JDBC non trovato", e);
        } catch (SQLException e) {LOGGER.error("Connessione al database non riuscita", e);
        }
        return connection;
    }
}
