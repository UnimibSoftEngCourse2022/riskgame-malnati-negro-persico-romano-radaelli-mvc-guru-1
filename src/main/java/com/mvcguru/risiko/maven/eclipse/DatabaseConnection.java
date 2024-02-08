package com.mvcguru.risiko.maven.eclipse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String SQLITE_DB_URL = "jdbc:sqlite:mydatabase.db";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(SQLITE_JDBC_DRIVER);

            connection = DriverManager.getConnection(SQLITE_DB_URL);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC non trovato");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connessione al database non riuscita");
            e.printStackTrace();
        }
        return connection;
    }
}
