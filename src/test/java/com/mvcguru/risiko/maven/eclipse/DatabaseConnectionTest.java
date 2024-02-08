package com.mvcguru.risiko.maven.eclipse;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseConnectionTest {

    @Test
    public void testGetConnection() {
        Connection connection = DatabaseConnection.getConnection();
        Assert.assertNotNull("Connection should not be null", connection);
        
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close connection");
            e.printStackTrace();
        }
    }
}
