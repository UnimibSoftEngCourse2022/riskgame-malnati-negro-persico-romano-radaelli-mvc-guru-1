package com.mvcguru.risiko.maven.eclipse.service.database;

import java.io.IOException;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

public interface DataDao {
	//UserDao
    User getUserByUsernameAndPassword(String username, String password) throws UserException;
    void insertUser(User user) throws UserException, GameException;
    void deleteUser(User user) throws UserException;	
    //GameDao
    IGame getGameById(String gameId) throws GameException, IOException;
    void insertGame(IGame game) throws GameException;
    void deleteGame(IGame game) throws GameException;
	void updateState(String gameId, GameState newState) throws GameException;
    List<IGame> getAllGames() throws GameException, IOException;
	
    //PlayerDao
	void insertPlayer(Player player) throws GameException;
	void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException;
	void updatePlayerColor(Player player) throws GameException;
	void deletePlayer(String username) throws GameException;
	List<Player> getPlayerInGame(String gameId) throws GameException;
	void updatePlayerObjective(String username, ICard objective) throws GameException;
	
	//TerritoryDao
	void insertTerritory(Territory territory, String gameId) throws GameException;
	void deleteTerritory(String name) throws GameException;
	void updateTerritoryOwner(String territoryName, Player player) throws GameException;
	void updateTerritoryArmies(String territoryName, int armies) throws GameException;
	List<Territory> getAllTerritories(String player) throws GameException;
	
	//TurnDao
	void insertTurn(Turn turn) throws GameException;
	void deleteTurn(Turn turn) throws GameException;
	void updateTurnIndex(Turn turn, int index) throws GameException;
	void updateTurnNumberOfTroops(Turn turn, int numberOfTroops) throws GameException;
	void updateNumAttackDice(Turn turn, int numAttackDice) throws GameException;
	void updateNumDefenseDice(Turn turn, int numDefenseDice) throws GameException;
	void updateDefenderTerritory(Turn turn, String defenderTerritory) throws GameException;
	void updateAttackerTerritory(Turn turn, String attackerTerritory) throws GameException;
	void updateIsConquered(Turn turn, boolean isConquered) throws GameException;
	Turn getLastTurnByGameId(String gameId) throws GameException;
	
	//ComboCardDao
	void insertComboCard(TerritoryCard t, Player owner, String gameId) throws GameException;
	void deleteComboCard(TerritoryCard t, Player owner, String gameId) throws GameException;
	void updateOwner(TerritoryCard t, String player, String gameId) throws GameException;
	List<TerritoryCard> getAllComboCards(String player, String gameId) throws GameException;
	
	void closeConnection();
}
