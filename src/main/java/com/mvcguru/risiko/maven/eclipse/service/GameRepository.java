package com.mvcguru.risiko.maven.eclipse.service;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.service.database.DaoSQLiteImpl;
import com.mvcguru.risiko.maven.eclipse.service.database.DatabaseConnection;
import com.mvcguru.risiko.maven.eclipse.service.database.GameDao;

public class GameRepository {
	private static GameRepository instance;
	private GameDao db;
	
	public GameRepository() throws DatabaseConnectionException, GameException, UserException {
		super();
		this.db = DaoSQLiteImpl.getInstance();
		System.out.println("Costruttore");
	}
	
	public static synchronized GameRepository getInstance() throws DatabaseConnectionException, GameException, UserException {
		 if (instance == null) {
			 System.out.println("Getinstance");
	            instance = new GameRepository();
	        }
	        return instance;
	}
	
	public synchronized void registerGame(IGame game) throws GameException {
		if (game != null) {
			
			db.registerGame(game);
			}
		else
			System.out.println("Game is null");
	}
	
	public synchronized void deleteGame(IGame game) throws GameException {
		if (game != null)
			db.deleteGame(game);
		else
			System.out.println("Game is null");
	}
	
	public synchronized IGame getGameById(String gameId) throws GameException {
		return db.getGameById(gameId);
	}
	
	public synchronized List<IGame> getAllGames() throws GameException {
		return db.getAllGames();
	}
	
	
}
