package com.mvcguru.risiko.maven.eclipse.service.database;

import java.io.IOException;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

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
	void updateState(String gameId, GameState newState) throws GameException;
    List<IGame> getAllGames() throws GameException, IOException;
	//PlayerDao
	void insertPlayer(Player player) throws GameException;
	void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException;
	void updatePlayerColor(String username, Player.PlayerColor color) throws GameException;
	void deletePlayer(String username) throws GameException;
	List<Player> getPlayerInGame(String gameId) throws GameException;
	void createPlayerTable() throws GameException;
	//TerritoryDao
	void insertTerritory(Territory territory, String gameId) throws GameException;
	void deleteTerritory(String name) throws GameException;
	void createTerritoryTable() throws GameException;
	void updateTerritoryOwner(String territoryName, Player player) throws GameException;
	void updateTerritoryArmies(String territoryName, int armies) throws GameException;
	List<Territory> getAllTerritories(String player) throws GameException;
	void closeConnection();

}
