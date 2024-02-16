package com.mvcguru.risiko.maven.eclipse.service.database;

import java.io.IOException;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

public interface DataDao {
	//UserDao
    User getUserByUsernameAndPassword(String username, String password) throws UserException;
    void registerUser(User user) throws UserException;
    void deleteUser(User user) throws UserException;
    void createUsersTable() throws UserException;
	//GameDao
    IGame getGameById(String gameId) throws GameException, IOException;
    void registerGame(IGame game) throws GameException;
    void deleteGame(IGame game) throws GameException;
    void createGamesTable() throws GameException;
    List<IGame> getAllGames() throws GameException, IOException;
	//PlayerDao
	void insertPlayer(Player player) throws GameException;
	void deletePlayer(String username) throws GameException;
	List<Player> getPlayerInGame(String gameId) throws GameException;
	void createPlayerTable() throws GameException;
	

	void closeConnection();
}
