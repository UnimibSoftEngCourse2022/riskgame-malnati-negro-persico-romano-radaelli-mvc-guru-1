package com.mvcguru.risiko.maven.eclipse.service.database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DatabaseConnectionTest {

    private static Connection connection;
    private static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String SQLITE_DB_URL = "jdbc:sqlite:mydatabase.db";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        connection = DatabaseConnection.getDefaultConnection();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testGetDefaultConnection() {
        assertNotNull(connection);
        try {
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }

    @Test
    void testGetSqliteDbUrl() {
        assertEquals(SQLITE_DB_URL, DatabaseConnection.getSqliteDbUrl());
    }

    @Test
    void testGetSqliteJdbcDriver() {
        assertEquals(SQLITE_JDBC_DRIVER, DatabaseConnection.getSqliteJdbcDriver());
    }

    @Test
    void testConnectionClose() {
        Connection testConnection = null;
        try {
            testConnection = DatabaseConnection.getDefaultConnection();
            assertNotNull(testConnection);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(testConnection);
            try {
                assertTrue(testConnection.isClosed());
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }
        }
    }
}
