
	package com.mvcguru.risiko.maven.eclipse;

	import java.util.List;
	import com.mvcguru.risiko.maven.eclipse.exception.GameException;
	import com.mvcguru.risiko.maven.eclipse.model.Game;

	public interface GameDao {
	    Game getGameById(int gameId) throws GameException;
	    void registerGame(Game game) throws GameException;
	    void deleteGame(Game game) throws GameException;
	    void createGamesTable();
	    List<Game> getAllGames() throws GameException;
	}


