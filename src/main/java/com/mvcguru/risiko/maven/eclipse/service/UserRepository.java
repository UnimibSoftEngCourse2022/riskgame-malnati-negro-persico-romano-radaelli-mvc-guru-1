package com.mvcguru.risiko.maven.eclipse.service;

import com.mvcguru.risiko.maven.eclipse.DatabaseConnection;
import com.mvcguru.risiko.maven.eclipse.UserDao;
import com.mvcguru.risiko.maven.eclipse.UserDaoSQLiteImpl;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.User;

public class UserRepository {
	private UserDao db;
	private static UserRepository instance;
	
	
	public UserRepository() throws DatabaseConnectionException, UserException {
		super();
		this.db = new UserDaoSQLiteImpl(DatabaseConnection.getSqliteDbUrl());
	}
	
	public static synchronized UserRepository getInstance() throws DatabaseConnectionException, UserException {
		 if (instance == null) {
	            instance = new UserRepository();
	        }
	        return instance;
	}
	
	public synchronized void registerUser(User user) throws UserException {
		if(user != null)
			db.registerUser(user);
		else
			System.out.println("User is null");
	}
	
	public synchronized User getUser(User user) throws UserException {
		User result = db.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
		return result;
	}
	
	public synchronized String getUserName(User user) throws UserException {
		String result = db.getUserByUsernameAndPassword(user.getUsername(), user.getPassword()).getUsername();
		return result;
	}
	
	public synchronized String getUserPassword(User user) throws UserException {
		String result = db.getUserByUsernameAndPassword(user.getUsername(), user.getPassword()).getPassword();
		return result;
	}
}
