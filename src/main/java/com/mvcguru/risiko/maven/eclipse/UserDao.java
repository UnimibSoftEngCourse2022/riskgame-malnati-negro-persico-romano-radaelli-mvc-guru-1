package com.mvcguru.risiko.maven.eclipse;

import java.sql.Connection;

public interface UserDao {
    User getUserByUsernameAndPassword(String username, String password) throws UserException;
    void registerUser(User user) throws UserException;
    void deleteUser(User user) throws UserException;
    void createUsersTable() throws UserException;
    Connection getConnection();
    void closeConnection();
}
