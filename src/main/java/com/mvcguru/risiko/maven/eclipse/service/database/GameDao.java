
	package com.mvcguru.risiko.maven.eclipse.service.database;

	import java.util.List;
	import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;

	public interface GameDao {
	    IGame getGameById(int gameId) throws GameException;
	    void registerGame(IGame game) throws GameException;
	    void deleteGame(IGame game) throws GameException;
	    void createGamesTable() throws GameException;
	    List<IGame> getAllGames() throws GameException;
	    void closeConnection();
	}


