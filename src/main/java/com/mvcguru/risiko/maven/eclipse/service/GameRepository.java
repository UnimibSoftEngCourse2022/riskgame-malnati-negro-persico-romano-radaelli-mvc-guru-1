package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.database.DaoSQLiteImpl;
import com.mvcguru.risiko.maven.eclipse.service.database.DataDao;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

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
		List<Territory> territories = new ArrayList<Territory>();
		for (Player p : lista) { 
			game.addPlayer(p);  
			territories = db.getAllTerritories(p.getUserName()); 
			p.setTerritories(territories);
			}

		return game;
	}
	
	public synchronized List<IGame> getAllGames() throws GameException, FullGameException, IOException {
		List<IGame> games = db.getAllGames();
		for (IGame g : games ) {
			List<Player> lista = db.getPlayerInGame(g.getId());
			for (Player p : lista) {
				g.addPlayer(p);
			}
		}
		return games;
	}
	
	public synchronized void updateState(String gameId, GameState newState) throws GameException{
		db.updateState(gameId, newState);
	}
    
	
	public synchronized void addPlayer(Player player) throws GameException {
		db.insertPlayer(player);
	}
	
	public synchronized void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException {
		db.updateSetUpCompleted(username, setUpCompleted);
	}
	
	public synchronized void removePlayer(String username) throws GameException {
		db.deletePlayer(username);
	}
	
	public synchronized void insertTerritory(Territory territory, String gameId) throws GameException{
		db.insertTerritory(territory, gameId);
	}
	
	public synchronized void deleteTerritory(String name) throws GameException{
		db.deleteTerritory(name);
	}
	
	public synchronized void updateTerritoryOwner(String territoryName, Player player) throws GameException{
		db.updateTerritoryOwner(territoryName, player);
	}
	
	public synchronized void updateTerritoryArmies(String territoryName, int armies) throws GameException {
		db.updateTerritoryArmies(territoryName, armies);
	}
	
	public synchronized List<Territory> getAllTerritories(String player) throws GameException {
		return db.getAllTerritories(player);
	}

	
}
