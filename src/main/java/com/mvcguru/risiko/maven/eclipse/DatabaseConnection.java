package com.mvcguru.risiko.maven.eclipse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;

public class DatabaseConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String SQLITE_DB_URL = "jdbc:sqlite:mydatabase.db";
    
    private DatabaseConnection() {
    }
    
    public static Connection getConnection(String jdbcDriver, String dbUrl) throws ClassNotFoundException, SQLException, DatabaseConnectionException {
        Connection connection = null;
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(dbUrl);
        }catch (ClassNotFoundException e) {
            LOGGER.error("Driver JDBC non trovato", e);
            throw new DatabaseConnectionException("Impossibile trovare il driver JDBC");
        } catch (SQLException e) {
            LOGGER.error("Connessione al database non riuscita", e);
            throw new DatabaseConnectionException("Impossibile connettersi al database");
        }

        return connection;
    }
    
    public static Connection getDefaultConnection() throws ClassNotFoundException, SQLException, DatabaseConnectionException {
        return getConnection(SQLITE_JDBC_DRIVER, SQLITE_DB_URL);
    }

    public static String getSqliteDbUrl() {
        return SQLITE_DB_URL;
    }

    public static String getSqliteJdbcDriver() {
        return SQLITE_JDBC_DRIVER;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {LOGGER.error("Errore durante la chiusura della connessione al database", e);
            }
        }
    }
}
