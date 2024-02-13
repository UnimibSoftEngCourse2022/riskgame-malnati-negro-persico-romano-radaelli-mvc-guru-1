package com.mvcguru.risiko.maven.eclipse.service;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.database.DaoSQLiteImpl;
import com.mvcguru.risiko.maven.eclipse.service.database.DataDao;

public class GameRepository {
	private static GameRepository instance;
	private DataDao db;
	
	public GameRepository() throws DatabaseConnectionException, GameException, UserException {
		super();
		this.db = DaoSQLiteImpl.getInstance();
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
	
	public synchronized IGame getGameById(String gameId) throws GameException, FullGameException {
		IGame game = db.getGameById(gameId);
		List<Player> lista = db.getPlayerInGame(gameId);
		for (Player p : lista) {
			game.addPlayer(p);
			}
		return game;
	}
	
	public synchronized List<IGame> getAllGames() throws GameException {
		return db.getAllGames();
	}
	
	public synchronized void add(Player player) throws GameException {
		db.insertPlayer(player);
	}
	
	public synchronized void remove(String username) throws GameException {
		db.deletePlayer(username);
	}
	
}
