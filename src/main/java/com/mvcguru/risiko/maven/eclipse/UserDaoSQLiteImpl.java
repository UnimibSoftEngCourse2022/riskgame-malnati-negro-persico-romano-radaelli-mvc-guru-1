package com.mvcguru.risiko.maven.eclipse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDaoSQLiteImpl implements UserDao {
    private Connection connection;

    public UserDaoSQLiteImpl(String dbUrl) {
        try {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la connessione al database.", e);
        }
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dell'utente.", e);
        }
    }

    @Override
    public void registerUser(User user) {
    	
    	if(user != null)
    		System.out.println("User regestring: " + user.getUsername() + " " + user.getPassword());
		else
			System.out.println("User is null");
    	String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
            System.out.println("bloco try");
        } catch (SQLException e) {
            System.out.println("bloco catch");
            throw new RuntimeException("Errore durante la registrazione dell'utente.", e);
        }
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'utente.", e);
        }
    }

    @Override
    public void deleteUser(User user) {
        String sql = "DELETE FROM users WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'utente.", e);
        }
    }
    
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	username text PRIMARY KEY,\n"
                + "	password text NOT NULL\n"
                + ");";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la creazione della tabella users.", e);
        }
    }


}
