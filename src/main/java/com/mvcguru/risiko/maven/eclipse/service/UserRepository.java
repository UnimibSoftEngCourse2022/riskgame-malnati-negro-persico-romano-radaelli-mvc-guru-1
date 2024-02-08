package com.mvcguru.risiko.maven.eclipse.service;

import com.mvcguru.risiko.maven.eclipse.DatabaseConnection;
import com.mvcguru.risiko.maven.eclipse.User;
import com.mvcguru.risiko.maven.eclipse.UserDao;
import com.mvcguru.risiko.maven.eclipse.UserDaoSQLiteImpl;

public class UserRepository {
	private UserDao db;
	private static UserRepository instance;
	
	public UserRepository() {
		super();
		this.db = new UserDaoSQLiteImpl(DatabaseConnection.SQLITE_JDBC_DRIVER);
	}
	
	public static synchronized UserRepository getInstance() {
		 if (instance == null) {
	            instance = new UserRepository();
	        }
	        return instance;
	}
	
	public synchronized void registerUser(User user) {
		db.registerUser(user);
	}
	
	public synchronized User getUser(User user) {
		User result = db.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
		return result;
	}
	
	public synchronized String getUserName(User user) {
		String result = db.getUserByUsernameAndPassword(user.getUsername(), user.getPassword()).getUsername();
		return result;
	}
	
	public synchronized String getUserPassword(User user) {
		String result = db.getUserByUsernameAndPassword(user.getUsername(), user.getPassword()).getPassword();
		return result;
	}
}
