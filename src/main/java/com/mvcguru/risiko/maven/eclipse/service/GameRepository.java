package com.mvcguru.risiko.maven.eclipse.service;

import java.io.IOException;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.database.DataDao;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import com.mvcguru.risiko.maven.eclipse.model.deck.TerritoriesDeck;

public class GameRepository {
	private static GameRepository instance;
	private DataDao db;
	
	public GameRepository() throws DatabaseConnectionException, GameException, UserException {
		super();
		this.db = DataDao.getInstance();
	}
	
	public static synchronized GameRepository getInstance() throws DatabaseConnectionException, GameException, UserException {
		 if (instance == null) { instance = new GameRepository();} return instance;
	}
	
	public synchronized IGame getCompletedGame(String gameId) throws GameException, FullGameException, IOException {
		IGame game = getGame(gameId);
		List<Player> lista = getAllPlayers(gameId);
		List<Territory> territories = null;
		List<TerritoryCard> comboCards = null;
		game.setCurrentTurn(getLastTurn(gameId));
		for (Player p : lista) {  
			territories = getAllTerritories(p.getUserName(), gameId); 
			comboCards = getAllComboCards(p.getUserName(), gameId);
			((TerritoriesDeck)game.getDeckTerritory()).getCards().removeAll(comboCards);
			p.setTerritories(territories);
			p.setComboCards(comboCards);
			game.addPlayer(p); 
			}

		return game;
	
	}
	
	
	
	//GameDao
	public synchronized void insertGame(IGame game) throws GameException {
		if (game != null) 
			db.insertGame(game);
	}
	
	public synchronized void deleteGame(IGame game) throws GameException {
		if (game != null)
			db.deleteGame(game);
	}
	
	public synchronized IGame getGame(String gameId) throws GameException, IOException {
		return db.getGame(gameId);
	}
	
	public synchronized List<IGame> getAllGames() throws GameException, FullGameException, IOException {
		List<IGame> games = db.getAllGames();
		for (IGame g : games ) {
			List<Player> lista = getAllPlayers(g.getId());
			for (Player p : lista) {
				g.addPlayer(p);
			}
		}
		return games;
	}
	
	public synchronized void updateState(String gameId, GameState newState) throws GameException{
		db.updateState(gameId, newState);
	}
    
	
	
	//PlayerDao
	public synchronized void addPlayer(Player player) throws GameException {
		db.insertPlayer(player);
	}

	public synchronized void removePlayer(String username) throws GameException {
		db.deletePlayer(username);
	}
	
	public synchronized Player getPlayer(String username, String gameId) throws GameException, IOException {
		return db.getPlayer(username, gameId);
	}
	
	public synchronized List<Player> getAllPlayers(String gameId) throws GameException, IOException {
		return db.getAllPlayers(gameId);
	}
	
	public synchronized void updateObjective(String username, String description) throws GameException {
		db.updatePlayerObjective(username, description);
	}
	
	
	public synchronized void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException {
		db.updateSetUpCompleted(username, setUpCompleted);
	}
	
	public synchronized void updatePlayerColor(Player player) throws GameException {
		db.updatePlayerColor(player);
	}
	
	
	
	//TerritoryDao
	public synchronized void insertTerritory(Territory territory, String gameId) throws GameException{
		db.insertTerritory(territory, gameId);
	}
	
	public synchronized void deleteTerritory(Territory t) throws GameException{
		db.deleteTerritory(t);
	}
	
	public synchronized Territory getTerritory(String territoryName, String player, String gameId) throws GameException {
		return db.getTerritory(territoryName, player, gameId);
	}
	
	public synchronized List<Territory> getAllTerritories(String player, String gameId) throws GameException {
		return db.getAllTerritories(player, gameId);
	}
	
	public synchronized void updateTerritoryOwner(String territoryName, Player player) throws GameException{
		db.updateTerritoryOwner(territoryName, player);
	}
	public synchronized void updateTerritoryArmies(String territoryName, String gameId, int armies) throws GameException {
		db.updateTerritoryArmies(territoryName, gameId, armies);
	}
	
	
	//TurnDao	
	public synchronized void insertTurn(Turn turn) throws GameException {
		db.insertTurn(turn);
	}
	
	public synchronized void deleteTurn(Turn turn) throws GameException {
		db.deleteTurn(turn);
	}
	
	public synchronized Turn getTurn(String gameId, int index) throws GameException, IOException {
		return db.getTurn(gameId, index);
	}
	
	public synchronized Turn getLastTurn(String gameId) throws GameException, IOException {
		return db.getLastTurn(gameId);
	}
	
	public synchronized void updateNumAttackDice(Turn turn, int numAttackDice) throws GameException {
		db.updateNumAttackDice(turn, numAttackDice);
	}
	
	public synchronized void updateNumDefenseDice(Turn turn, int numDefenseDice) throws GameException {
		db.updateNumDefenseDice(turn, numDefenseDice);
	}
	
	public synchronized void updateIsConquered(Turn turn, boolean isConquered) throws GameException {
		db.updateIsConquered(turn, isConquered);
	}
	
	public synchronized void updateTurnNumberOfTroops(Turn turn,int numberOfTroops) throws GameException {
		db.updateTurnNumberOfTroops(turn, numberOfTroops);
	}
	
	public synchronized void updateDefenderTerritory(Turn turn, Territory defenderTerritory) throws GameException {
		db.updateDefenderTerritory(turn, defenderTerritory.getName());
	}
	
	public synchronized void updateAttackerTerritory(Turn turn, Territory attackerTerritory) throws GameException {
		db.updateAttackerTerritory(turn, attackerTerritory.getName());
	}
	
	
	//ComboCardDao
	public synchronized void insertComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
        db.insertComboCard(t, owner, gameId);
    }
	
	public synchronized void deleteComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
		db.deleteComboCard(t, owner, gameId);
	}
	
	public synchronized TerritoryCard getComboCard(String player, String territory, String gameId) throws GameException {
		return db.getComboCard(player, territory, gameId);
	}
	
	public synchronized void updateOwner(TerritoryCard t, String player, String gameId) throws GameException {
		db.updateOwner(t, player, gameId);
	}
	
	public synchronized List<TerritoryCard> getAllComboCards(String player, String gameId) throws GameException {
		return db.getAllComboCards(player, gameId);
	}
}
