
	package com.mvcguru.risiko.maven.eclipse.service.database;

	import java.util.List;
	import com.mvcguru.risiko.maven.eclipse.exception.GameException;
	import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.IGame;

	public interface GameDao {
	    IGame getGameById(int gameId) throws GameException;
	    void registerGame(IGame game) throws GameException;
	    void deleteGame(IGame game) throws GameException;
	    void createGamesTable();
	    List<IGame> getAllGames() throws GameException;
	}


