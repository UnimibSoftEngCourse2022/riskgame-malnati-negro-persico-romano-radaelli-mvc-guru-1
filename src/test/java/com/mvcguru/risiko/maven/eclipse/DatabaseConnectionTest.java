package com.mvcguru.risiko.maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

class DatabaseConnectionTest {

    @Test
    void testGetConnection() {
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
