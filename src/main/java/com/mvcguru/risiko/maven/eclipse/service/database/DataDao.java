package com.mvcguru.risiko.maven.eclipse.service.database;

import java.io.IOException;
import java.util.ArrayList;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

public interface DataDao {
	//UserDao
    User getUser(String username, String password) throws UserException;
    void insertUser(User user) throws UserException, GameException;
    void deleteUser(User user) throws UserException;	
    //GameDao
    IGame getGame(String gameId) throws GameException, IOException;
    void insertGame(IGame game) throws GameException;
    void deleteGame(IGame game) throws GameException;
	void updateState(String gameId, GameState newState) throws GameException;
	ArrayList<IGame> getAllGames() throws GameException, IOException;
	
    //PlayerDao
	void insertPlayer(Player player) throws GameException;
	void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException;
	void updatePlayerColor(Player player) throws GameException;
	void deletePlayer(String username) throws GameException;
	ArrayList<Player> getAllPlayers(String gameId) throws GameException, IOException;
	Player getPlayer(String username, String gameId) throws GameException, IOException;
	void updatePlayerObjective(String username, ObjectiveCard objective) throws GameException;
	
	//TerritoryDao
	void insertTerritory(Territory territory, String gameId) throws GameException;
	void deleteTerritory(Territory territory) throws GameException;
	Territory getTerritory(String territoryName, String player, String gameId) throws GameException;
	void updateTerritoryOwner(String territoryName, Player player) throws GameException;
	void updateTerritoryArmies(String territoryName, String gameId, int troops) throws GameException;
	ArrayList<Territory> getAllTerritories(String player, String gameId) throws GameException;
	
	//TurnDao
	void insertTurn(Turn turn) throws GameException;
	void deleteTurn(Turn turn) throws GameException;
	void updateTurnNumberOfTroops(Turn turn, int numberOfTroops) throws GameException;
	void updateNumAttackDice(Turn turn, int numAttackDice) throws GameException;
	void updateNumDefenseDice(Turn turn, int numDefenseDice) throws GameException;
	void updateDefenderTerritory(Turn turn, String defenderTerritory) throws GameException;
	void updateAttackerTerritory(Turn turn, String attackerTerritory) throws GameException;
	void updateIsConquered(Turn turn, boolean isConquered) throws GameException;
	Turn getTurn(String gameId, int index) throws GameException, IOException;
	Turn getLastTurn(String gameId) throws GameException, IOException;
	
	//ComboCardDao
	void insertComboCard(TerritoryCard t, Player owner, String gameId) throws GameException;
	void deleteComboCard(TerritoryCard t, Player owner, String gameId) throws GameException;
	void updateOwner(TerritoryCard t, String player, String gameId) throws GameException;
	TerritoryCard getComboCard(String player, String territory, String gameId) throws GameException;
	ArrayList<TerritoryCard> getAllComboCards(String player, String gameId) throws GameException;
	
	void closeConnection();
	
	static DaoSQLiteImpl getInstance() throws DatabaseConnectionException {
		return DaoSQLiteImpl.getInstance();
	}
	
}
