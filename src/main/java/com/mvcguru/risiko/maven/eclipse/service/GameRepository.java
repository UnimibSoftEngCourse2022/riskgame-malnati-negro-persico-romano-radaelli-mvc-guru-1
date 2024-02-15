package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
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
		 if (instance == null) { instance = new GameRepository();} return instance;
	}
	
	public synchronized void registerGame(IGame game) throws GameException {
		if (game != null) 
			db.registerGame(game);
	}
	
	public synchronized void deleteGame(IGame game) throws GameException {
		if (game != null)
			db.deleteGame(game);
	}
	
	public synchronized IGame getGameById(String gameId) throws GameException, FullGameException, IOException {
		IGame game = db.getGameById(gameId);
		List<Player> lista = db.getPlayerInGame(gameId);
		for (Player p : lista) { game.addPlayer(p); }
		return game;
	}
	
	public synchronized List<IGame> getAllGames() throws GameException, IOException {
		return db.getAllGames();
	}
	
	public synchronized void addPlayer(Player player) throws GameException {
		db.insertPlayer(player);
	}
	
	public synchronized void removePlayer(String username) throws GameException {
		db.deletePlayer(username);
	}
	
}
