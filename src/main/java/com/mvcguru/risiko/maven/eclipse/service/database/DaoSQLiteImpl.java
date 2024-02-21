package com.mvcguru.risiko.maven.eclipse.service.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import com.mvcguru.risiko.maven.eclipse.model.deck.ObjectivesDeck;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.states.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;

public class DaoSQLiteImpl implements DataDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoSQLiteImpl.class);
    private Connection connection;
    private static DaoSQLiteImpl instance;
    
    private DaoSQLiteImpl(String dbUrl) throws DatabaseConnectionException {
        try {
            connection = DriverManager.getConnection(dbUrl);
            initializeDatabase();
            if (connection.isClosed()) {
                throw new DatabaseConnectionException("Connessione al database non riuscita");
            }
        } catch (SQLException e) {
            LOGGER.error("Errore durante la connessione al database", e);
        }
    }

    public static synchronized DaoSQLiteImpl getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            instance = new DaoSQLiteImpl(DatabaseConnection.getSqliteDbUrl());
        }
        return instance;
    }
    
    private PreparedStatement prepareStatement(String sql, String... parameters) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setString(i + 1, parameters[i]);
            }
        } catch (SQLException e) {if (pstmt != null) {pstmt.close();}throw e;
        }
        return pstmt;
    }
    
    private void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                LOGGER.error("Error closing PreparedStatement", e);
            }
        }
    }

    public void initializeDatabase() {
        createTable("CREATE TABLE IF NOT EXISTS users (username text PRIMARY KEY, password text NOT NULL);");
        createTable("CREATE TABLE IF NOT EXISTS games (gameId TEXT PRIMARY KEY, mode TEXT NOT NULL, number_of_players INTEGER NOT NULL, idMap TEXT NOT NULL, state TEXT NOT NULL, winner TEXT)");
        createTable("CREATE TABLE IF NOT EXISTS players (username TEXT, gameId TEXT, color TEXT, objective TEXT, setUpCompleted BOOLEAN NOT NULL, FOREIGN KEY(gameId) REFERENCES games(gameId), PRIMARY KEY (username, gameId));");
        createTable("CREATE TABLE IF NOT EXISTS territories (name TEXT, gameId TEXT, owner TEXT, continent INTEGER, armies INTEGER, svgPath TEXT, FOREIGN KEY(owner) REFERENCES players(username), FOREIGN KEY(gameId) REFERENCES games(gameId), PRIMARY KEY (name, gameId));");
        createTable("CREATE TABLE IF NOT EXISTS turns (indexTurn INTEGER, player TEXT, gameId TEXT, numberOfTroops INTEGER, attackerTerritory TEXT, defenderTerritory TEXT, numAttackDice INTEGER, numDifenceDice INTEGER, isConquered BOOLEAN NOT NULL, FOREIGN KEY(player) REFERENCES players(username), FOREIGN KEY(gameId) REFERENCES games(gameId), PRIMARY KEY (indexTurn, gameId));");
        createTable("CREATE TABLE IF NOT EXISTS comboCards (player TEXT, gameId TEXT, territory TEXT, symbol TEXT, FOREIGN KEY(player) REFERENCES players(username), FOREIGN KEY(gameId) REFERENCES games(gameId), FOREIGN KEY(territory) REFERENCES territories(name), PRIMARY KEY (player, gameId, territory));");
    }

    private void createTable(String sql) {
    	PreparedStatement pstmt = null;
        try {
        	pstmt = connection.prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {
        	LOGGER.error("Errore durante la creazione della tabella", e);
		} finally {
			closePreparedStatement(pstmt);
		}
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Errore durante la chiusura della connessione al database", e);
            }
        }
    }

    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }
    
    
// ----------------------------------------------------------------------------------------------------------------
// ------------------------ CREATE --------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
    
    private void executeInsert(String sql, Object... values) throws GameException {
    	PreparedStatement pstmt = null;
        try {
    		pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {throw new GameException("Errore durante l'inserimento", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
    @Override
    public void insertUser(User user) throws UserException, GameException {
        executeInsert("INSERT INTO users(username, password) VALUES(?, ?)", user.getUsername(), user.getPassword());
    }

    @Override
    public void insertGame(IGame game) throws GameException {
        executeInsert("INSERT INTO games (gameId, mode, number_of_players, idMap, state, winner) VALUES (?, ?, ?, ?, ?, ?)",
                      game.getId(), game.getConfiguration().getMode().name(), game.getConfiguration().getNumberOfPlayers(),
                      game.getConfiguration().getIdMap(), game.getState().getClass().getSimpleName(), game.getWinner());
    }

    @Override
    public void insertPlayer(Player player) throws GameException {
        executeInsert("INSERT INTO players (username, gameId, color, setUpCompleted) VALUES (?, ?, ?, ?)",
                      player.getUserName(), player.getGameId(), player.getColor().name(), player.isSetUpCompleted());
    }

    @Override
    public void insertTerritory(Territory territory, String gameId) throws GameException {
        executeInsert("INSERT INTO territories (name, gameId, owner, continent, armies, svgPath) VALUES (?, ?, ?, ?, ?, ?)",
                      territory.getName(), gameId, territory.getIdOwner(), territory.getContinent(), territory.getArmies(), territory.getSvgPath());
    }

    @Override
    public void insertTurn(Turn turn) throws GameException {
        executeInsert("INSERT INTO turns (indexTurn, player, gameId, numberOfTroops, isConquered) VALUES (?, ?, ?, ?, ?)",
                      turn.getIndexTurn(), turn.getPlayer().getUserName(), turn.getPlayer().getGameId(), turn.getNumberOfTroops(), turn.isConquered());
    }

    @Override
    public void insertComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
        executeInsert("INSERT INTO comboCards (player, gameId, territory, symbol) VALUES (?, ?, ?, ?)",
                      owner.getUserName(), gameId, t.getTerritory().getName(), t.getSymbol().name());
    }

    
// ----------------------------------------------------------------------------------------------------------------
// ------------------------ READ ----------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
    
    @Override
    public User getUser(String username, String password) throws UserException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            pstmt = prepareStatement(sql, username, password);
            rs = pstmt.executeQuery();
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            if (rs.next()) {
                user = User.builder()
                			.username(username)
                			.password(password)
                			.build();
            }
		} catch (SQLException e) {
			LOGGER.error("Errore durante il recupero dell'utente", e);
        } finally {
            closePreparedStatement(pstmt);
        }
		return user;
    }
    
    @Override
	public IGame getGame(String gameId) throws GameException, IOException {
	   String sql = "SELECT * FROM games WHERE gameId = ?";
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    IGame game = null;
	    try {
	    	pstmt = prepareStatement(sql,gameId);
	        rs = pstmt.executeQuery();
	        pstmt.setString(1, gameId);
	        if (rs.next()) {
				game = extractGameFromResultSet(rs);
	        }
			return game;
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero dei giocatori.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
    }
   
    @Override
    public ArrayList<IGame> getAllGames() throws GameException, IOException {
        String sql = "SELECT * FROM games";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<IGame> games = new ArrayList<>();
        try {
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                games.add(getGame(rs.getString("gameId")));
            }
            rs.close();
            return games;
        } catch (SQLException e) {throw new GameException("Errore durante il recupero di tutte le partite", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
     
    @Override
	public Player getPlayer(String username, String gameId) throws GameException, IOException {
		String sql = "SELECT * FROM players WHERE username = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Player player = null;
		try {
			pstmt = prepareStatement(sql, username, gameId);
			rs = pstmt.executeQuery();
			pstmt.setString(1, username);
			pstmt.setString(2, gameId);
			if (rs.next()) {
				player = Player.builder()
								.userName(username)
								.gameId(gameId)
								.color(Player.PlayerColor.valueOf(rs.getString("color")))
								.territories(getAllTerritories(username, gameId))
								.objective(findObjectiveCard(rs.getString("objective"), gameId))
								.setUpCompleted(rs.getBoolean("setUpCompleted"))
								.build();
			}
			LOGGER.info("Player       sdsdsaadasdasdasdasd: {}", player);
			return player;
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero del giocatore.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    
    @Override
	public ArrayList<Player> getAllPlayers(String gameId) throws GameException, IOException {
        String sql = "SELECT * FROM players WHERE gameId = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Player> players = new ArrayList<>();
		try {
			pstmt = prepareStatement(sql, gameId);
			rs = pstmt.executeQuery();
			pstmt.setString(1, gameId);
			while (rs.next()) {
				Player player = getPlayer(rs.getString("username"), gameId);
				players.add(player);
			}
			return players;
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero dei giocatori.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
    }
    
    @Override
	public Territory getTerritory(String territoryName, String player, String gameId) throws GameException {
		String sql = "SELECT * FROM territories WHERE name = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Territory territory = null;
		try {
			pstmt = prepareStatement(sql, territoryName, gameId);
			rs = pstmt.executeQuery();
			pstmt.setString(1, territoryName);
			pstmt.setString(2, gameId);
			if (rs.next()) {
				int continent = rs.getInt("continent");
				int armies = rs.getInt("armies");
				String svgPath = rs.getString("svgPath");
				territory = Territory.builder().name(territoryName)
											.idOwner(player)
											.continent(continent)
											.armies(armies)
											.svgPath(svgPath)
											.build();
			}
			return territory;	
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero del territorio.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    
    @Override
    public ArrayList<Territory> getAllTerritories(String player, String gameId) throws GameException {
		String sql = "SELECT * FROM territories WHERE owner = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		ArrayList<Territory> result = new ArrayList<>();
		try {
			pstmt = prepareStatement(sql);
			pstmt.setString(1, player);
			pstmt.setString(2, gameId);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Territory territory = getTerritory(rs.getString("name"), player, gameId);
					result.add(territory);
				}
				return result;
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero dei territori.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    
    @Override
	public Turn getTurn(String gameId, int index) throws GameException, IOException {
		String sql = "SELECT * FROM turns WHERE indexTurn = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		Turn turn = null;
		try {
			pstmt = prepareStatement(sql);
			pstmt.setInt(1, index);
			pstmt.setString(2, gameId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String player = rs.getString("player");
                    int numberOfTroops = rs.getInt("numberOfTroops");
					String attackerTerritory = rs.getString("attackerTerritory");
					String defenderTerritory = rs.getString("defenderTerritory");
					int numAttackDice = rs.getInt("numAttackDice");
					int numDefenseDice = rs.getInt("numDifenceDice");
					boolean isConquered = rs.getBoolean("isConquered");
                    turn = Turn.builder()
                    			.indexTurn(index)
                            	.player(getPlayer(player, gameId))
                            	.numberOfTroops(numberOfTroops)
                            	.attackerTerritory(Territory.builder().name(attackerTerritory).build())
								.defenderTerritory(Territory.builder().name(defenderTerritory).build())
								.numAttDice(numAttackDice)
								.numDefDice(numDefenseDice)
								.isConquered(isConquered)
								.build();
				}
				return turn;
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero del turno.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    
    @Override
	public Turn getLastTurn(String gameId) throws GameException, IOException {
	    String sql = "SELECT * FROM turns WHERE gameId = ? ORDER BY indexTurn DESC LIMIT 1";
	    PreparedStatement pstmt = null;
	    Turn turn = null;
		try {
			pstmt = prepareStatement(sql);
			pstmt.setString(1, gameId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int index = rs.getInt("indexTurn");
					String player = rs.getString("player");
					int numberOfTroops = rs.getInt("numberOfTroops");
					String attackerTerritory = rs.getString("attackerTerritory");
					String defenderTerritory = rs.getString("defenderTerritory");
					int numAttackDice = rs.getInt("numAttackDice");
					int numDefenseDice = rs.getInt("numDifenceDice");
					boolean isConquered = rs.getBoolean("isConquered");
					turn = Turn.builder()
								.indexTurn(index)
								.player(getPlayer(player, gameId))
								.numberOfTroops(numberOfTroops)
								.attackerTerritory(Territory.builder().name(attackerTerritory).build())
								.defenderTerritory(Territory.builder().name(defenderTerritory).build())
								.numAttDice(numAttackDice)
								.numDefDice(numDefenseDice)
								.isConquered(isConquered)
								.build();
				}
				return turn;
			}
		} catch (SQLException e) {
			LOGGER.error("Errore durante il recupero dell'ultimo turno", e);
			return null;
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    
    @Override
	public TerritoryCard getComboCard(String player, String territory, String gameId) throws GameException {
		String sql = "SELECT * FROM comboCards WHERE player = ? AND territory = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		TerritoryCard card = null;
		try {
			pstmt = prepareStatement(sql);
			pstmt.setString(1, player);
			pstmt.setString(2, territory);
			pstmt.setString(3, gameId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					card = TerritoryCard.builder()
							.territory(getTerritory(territory, player, gameId))
							.symbol(CardSymbol.valueOf(rs.getString("symbol"))).build();
				}
				return card;
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero della carta combo.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    
    @Override
	public ArrayList<TerritoryCard> getAllComboCards(String player, String gameId) throws GameException {
		String sql = "SELECT * FROM comboCards WHERE player = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		ArrayList<TerritoryCard> result = new ArrayList<>();
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, player);
			pstmt.setString(2, gameId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					TerritoryCard card = TerritoryCard.builder()
														.territory(getTerritory(rs.getString("territory"), player, gameId))
														.symbol(CardSymbol.valueOf(rs.getString("symbol")))
														.build();
					result.add(card);
				}
				return result;
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero delle carte combo.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    

// ----------------------------------------------------------------------------------------------------------------
// ------------------------ UPDATE --------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
    
    private void executeUpdate(String sql, Object... params) throws GameException {
    	PreparedStatement pstmt = null;
        try {
        	pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            int updatedRows = pstmt.executeUpdate();
            if (updatedRows == 0) {
                LOGGER.error("Nessun elemento aggiornato: potrebbe non esistere un elemento con l'ID specificato. {} nome {}", sql, params);
            }
        } catch (SQLException e) {
	        throw new GameException("Errore durante l'aggiornamento.", e);
	    } finally {
	        closePreparedStatement(pstmt);
	    }
	}

    @Override
    public void updateState(String gameId, GameState newState) throws GameException {
        String sql = "UPDATE games SET state = ? WHERE gameId = ?";
        executeUpdate(sql, newState.getClass().getSimpleName(), gameId);
    }
    
    @Override
    public void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException {
        String sql = "UPDATE players SET setUpCompleted = ? WHERE username = ?";
        executeUpdate(sql, setUpCompleted, username);
    }

    @Override
    public void updatePlayerObjective(String username, String description) throws GameException {
        String sql = "UPDATE players SET objective = ? WHERE username = ?";
        executeUpdate(sql, description, username);
    }

    @Override
    public void updatePlayerColor(Player player) throws GameException {
        String sql = "UPDATE players SET color = ? WHERE username = ?";
        executeUpdate(sql, player.getColor().name(), player.getUserName());
    }

    @Override
    public void updateTerritoryOwner(String territoryName, Player player) throws GameException {
        String sql = "UPDATE territories SET owner = ? WHERE name = ? AND gameId = ?";
        executeUpdate(sql, player.getUserName(), territoryName, player.getGameId());
    }

    @Override
    public void updateTerritoryArmies(String territoryName, String gameId,  int troops) throws GameException {
        String sql = "UPDATE territories SET armies = ? WHERE name = ? AND gameId = ?";
        executeUpdate(sql, troops, territoryName, gameId);
    }
    
    @Override
	public void updateTurnNumberOfTroops(Turn turn, int numberOfTroops) throws GameException {
		String sql = "UPDATE turns SET numberOfTroops = ? WHERE indexTurn = ? AND gameId = ?";
		executeUpdate(sql, numberOfTroops, turn.getIndexTurn(), turn.getPlayer().getGameId());
	}
    
    @Override
    public void updateNumAttackDice(Turn turn, int numAttackDice) throws GameException {
        String sql = "UPDATE turns SET numAttackDice = ? WHERE indexTurn = ? AND gameId = ?";
        executeUpdate(sql, numAttackDice, turn.getIndexTurn(), turn.getPlayer().getGameId());
    }
    
    @Override
	public void updateAttackerTerritory(Turn turn, String attackerTerritory) throws GameException {
		String sql = "UPDATE turns SET attackerTerritory = ? WHERE indexTurn = ? AND gameId = ?";
		executeUpdate(sql, attackerTerritory, turn.getIndexTurn(), turn.getPlayer().getGameId());
	}
    
    @Override
    public void updateDefenderTerritory(Turn turn, String defenderTerritory) throws GameException {
        String sql = "UPDATE turns SET defenderTerritory = ? WHERE indexTurn = ? AND gameId = ?";
        executeUpdate(sql, defenderTerritory, turn.getIndexTurn(), turn.getPlayer().getGameId());
    }

    @Override
    public void updateNumDefenseDice(Turn turn, int numDefenseDice) throws GameException {
        String sql = "UPDATE turns SET numDifenceDice = ? WHERE indexTurn = ? AND gameId = ?";
        executeUpdate(sql, numDefenseDice, turn.getIndexTurn(), turn.getPlayer().getGameId());
    }

    @Override
    public void updateIsConquered(Turn turn, boolean isConquered) throws GameException {
        String sql = "UPDATE turns SET isConquered = ? WHERE indexTurn = ? AND gameId = ?";
        executeUpdate(sql, isConquered, turn.getIndexTurn(), turn.getPlayer().getGameId());
    }

    @Override
    public void updateOwner(TerritoryCard t, String player, String gameId) throws GameException {
        String sql = "UPDATE comboCards SET player = ? WHERE gameId = ? AND territory = ?";
        executeUpdate(sql, player, gameId, t.getTerritory().getName());
    }


// ----------------------------------------------------------------------------------------------------------------
// ------------------------ DELETE --------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
    
    private void executeDelete(String sql, Object... params) {
        PreparedStatement pstmt = null;
        try {
        	pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {LOGGER.error("Errore durante l'eliminazione {}, {}", sql, e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }

    @Override
    public void deleteUser(User user) throws UserException {
        String sql = "DELETE FROM users WHERE username = ?";
        executeDelete(sql, user.getUsername());
    }
    
    @Override
    public void deleteGame(IGame game) throws GameException {
        String sql = "DELETE FROM games WHERE gameId = ?";
        executeDelete(sql, game.getId());
    }

    @Override
    public void deletePlayer(String p) throws GameException {
        String sql = "DELETE FROM players WHERE username = ?";
        executeDelete(sql, p);
    }

    @Override
    public void deleteTerritory(Territory t) throws GameException {
        String sql = "DELETE FROM territories WHERE name = ?";
        executeDelete(sql, t.getName());
    }

    @Override
    public void deleteTurn(Turn turn) throws GameException {
        String sql = "DELETE FROM turns WHERE indexTurn = ? AND player = ? AND gameId = ?";
        executeDelete(sql, turn.getIndexTurn(), turn.getPlayer().getUserName(), turn.getPlayer().getGameId());
    }

    @Override
    public void deleteComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
        String sql = "DELETE FROM comboCards WHERE player = ? AND gameId = ? AND territory = ? AND symbol = ?";
        executeDelete(sql, owner.getUserName(), gameId, t.getTerritory().getName(), t.getSymbol().name());
    }


// ----------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
    
	private IGame extractGameFromResultSet(ResultSet rs) throws GameException, IOException{
		IGame newGame = null;
		GameConfiguration config = GameConfiguration.builder().build();
        try {
        	config.setMode(GameConfiguration.GameMode.valueOf(rs.getString("mode")));
        	config.setNumberOfPlayers(rs.getInt("number_of_players"));
	        config.setIdMap(rs.getString("idMap"));
	        
	        newGame = FactoryGame.getInstance().createGame(config);
	        newGame.setId(rs.getString("gameId"));
	        String stateName = rs.getString("state");
            switch (stateName) {
            case "GameSetupState":
                newGame.setState(GameSetupState.builder().game(newGame).build());
                break;
            case "StartTurnState":
                newGame.setState(StartTurnState.builder().game(newGame).build());
                break;
            case "LobbyState":
                newGame.setState(LobbyState.builder().game(newGame).build());
                break;
			case "BattleState":
				newGame.setState(BattleState.builder().game(newGame).build());
				break;
            default:
            	LOGGER.error("Stato non riconosciuto");
                break;
            }
		} catch (SQLException e) {throw new GameException("Errore durante il recupero di una partita", e); }
        
        return newGame;
	}
	
	private ObjectiveCard findObjectiveCard(String description, String gameId) throws GameException, IOException {
		IGame game = getGame(gameId);
		ObjectivesDeck objectiveCards = (ObjectivesDeck) game.getDeckObjective();
		ObjectiveCard cardReturn = null;
		LOGGER.info("Description: {}", description);
		List<ObjectiveCard> cards = new LinkedList<>(objectiveCards.getCards());
		LOGGER.info("Cards: {}", cards.size());
		for (ObjectiveCard card : cards) {
			if (card.getObjective().equals(description)) {
				cardReturn = card;
				LOGGER.error("Carta {} ------ trovata", description);
				return cardReturn;
			}
		}
		LOGGER.error("Carta {} non trovata", description);
		return cardReturn;
	}
}