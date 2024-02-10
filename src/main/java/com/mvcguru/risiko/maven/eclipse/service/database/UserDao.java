package com.mvcguru.risiko.maven.eclipse.service.database;

import java.sql.Connection;

import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;

public interface UserDao {
    User getUserByUsernameAndPassword(String username, String password) throws UserException;
    void registerUser(User user) throws UserException;
    void deleteUser(User user) throws UserException;
    void createUsersTable() throws UserException;
}
