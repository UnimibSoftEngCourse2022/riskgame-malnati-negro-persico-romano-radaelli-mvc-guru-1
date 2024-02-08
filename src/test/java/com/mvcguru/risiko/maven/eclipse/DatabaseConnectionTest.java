package com.mvcguru.risiko.maven.eclipse;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    public void testGetConnection() {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection, "Connection should not be null");
        
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close connection");
            e.printStackTrace();
        }
    }
}
