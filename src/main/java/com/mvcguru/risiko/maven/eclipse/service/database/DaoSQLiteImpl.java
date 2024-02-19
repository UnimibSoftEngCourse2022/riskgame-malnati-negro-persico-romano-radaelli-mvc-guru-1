package com.mvcguru.risiko.maven.eclipse.service.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
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
import lombok.experimental.SuperBuilder;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;

@SuperBuilder
public class DaoSQLiteImpl implements DataDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoSQLiteImpl.class);
    private static final String ERROR_LOG_PLAYER = "Nessun giocatore aggiornato: potrebbe non esistere un giocatore con lo username specificato.";
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
        createTable("CREATE TABLE IF NOT EXISTS games (gameId TEXT PRIMARY KEY, mode TEXT NOT NULL, number_of_players INTEGER NOT NULL, idMap TEXT NOT NULL, state TEXT NOT NULL);");
        createTable("CREATE TABLE IF NOT EXISTS players (username TEXT, gameId TEXT, color TEXT, objective TEXT, setUpCompleted BOOLEAN NOT NULL, FOREIGN KEY(gameId) REFERENCES games(gameId), PRIMARY KEY (username));");
        createTable("CREATE TABLE IF NOT EXISTS territories (name TEXT, gameId TEXT, player TEXT, continent INTEGER, armies INTEGER, svgPath TEXT, FOREIGN KEY(player) REFERENCES players(username), FOREIGN KEY(gameId) REFERENCES games(gameId), PRIMARY KEY (name, player, gameId));");
        createTable("CREATE TABLE IF NOT EXISTS turns (indexTurn INTEGER, player TEXT, gameId TEXT, numberOfTroops INTEGER, attackerTerritory TEXT, defenderTerritory TEXT, FOREIGN KEY(player) REFERENCES players(username), FOREIGN KEY(gameId) REFERENCES games(gameId), PRIMARY KEY (indexTurn, gameId));");
        createTable("CREATE TABLE IF NOT EXISTS comboCards (player TEXT, gameId TEXT, territory TEXT, symbol TEXT, FOREIGN KEY(player) REFERENCES players(username), FOREIGN KEY(gameId) REFERENCES games(gameId), FOREIGN KEY(territory) REFERENCES territories(name), PRIMARY KEY (player, gameId, territory, symbol));");
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
    
    // --------------------------------------------------------
    // ------------------------ INSERT ------------------------
    // --------------------------------------------------------
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
        executeInsert("INSERT INTO games (gameId, mode, number_of_players, idMap, state) VALUES (?, ?, ?, ?, ?)",
                      game.getId(), game.getConfiguration().getMode().name(), game.getConfiguration().getNumberOfPlayers(),
                      game.getConfiguration().getIdMap(), game.getState().getClass().getSimpleName());
    }

    public void insertPlayer(Player player) throws GameException {
        executeInsert("INSERT INTO players (username, gameId, color, setUpCompleted) VALUES (?, ?, ?, ?)",
                      player.getUserName(), player.getGameId(), player.getColor().name(), player.isSetUpCompleted());
    }

    public void insertTerritory(Territory territory, String gameId) throws GameException {
        executeInsert("INSERT INTO territories (name, player, gameId, continent, armies, svgPath) VALUES (?, ?, ?, ?, ?, ?)",
                      territory.getName(), territory.getIdOwner(), gameId, territory.getContinent(), territory.getArmies(), territory.getSvgPath());
    }

    public void insertTurn(Turn turn) throws GameException {
        executeInsert("INSERT INTO turns (indexTurn, player, gameId, numberOfTroops, attackerTerritory, defenderTerritory) VALUES (?, ?, ?, ?, ?, ?)",
                      turn.getIndexTurn(), turn.getPlayer().getUserName(), turn.getPlayer().getGameId(), turn.getNumberOfTroops(),
                      turn.getAttackerTerritory().getName(), turn.getDefenderTerritory().getName());
    }

    public void insertComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
        executeInsert("INSERT INTO comboCards (player, gameId, territory, symbol) VALUES (?, ?, ?, ?)",
                      owner.getUserName(), gameId, t.getTerritory().getName(), t.getSymbol().name());
    }


    // --------------------------------------------------------
    // ------------------------ READ --------------------------
    // --------------------------------------------------------
    @Override
    public User getUserByUsernameAndPassword(String username, String password) throws UserException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = prepareStatement(sql, username, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"));
            } else {
                return null;
            }
        } catch (SQLException e) {throw new UserException("Errore durante il recupero dell'utente.", e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {LOGGER.error("Error closing PreparedStatement", e);
            }
            closePreparedStatement(pstmt);
        }
    }
    
    @Override
	public IGame getGameById(String gameId) throws GameException, IOException {
		   String sql = "SELECT * FROM games WHERE gameId = ?";
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    IGame game = null;
		    
		    try {
		    	pstmt = prepareStatement(sql,gameId);
		        rs = pstmt.executeQuery();

		        if (rs.next()) {
		            game = extractGameFromResultSet(rs);
		            
		        }
		    } catch (SQLException e) {LOGGER.error("Errore durante il recupero del gioco con ID {}, {}", gameId, e);
		    }
		    return game;
	}
    
    @Override
    public List<IGame> getAllGames() throws GameException, IOException {
        String sql = "SELECT * FROM games";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<IGame> games = new ArrayList<>();
        try {
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                IGame game = extractGameFromResultSet(rs);
                games.add(game);
            }
            rs.close();
            return games;
        } catch (SQLException e) {throw new GameException("Errore durante il recupero di tutte le partite", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
    public List<Player> getPlayerInGame(String gameId) throws GameException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT username, gameId, color, objective, setUpCompleted FROM players WHERE gameId = ?";
        PreparedStatement pstmt = null;
        try{
        	pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, gameId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	String username = rs.getString("username");
                	String color = rs.getString("color");
                	boolean setUpCompleted = rs.getBoolean("setUpCompleted");
                	ICard objective = ObjectiveCard.builder().objective(rs.getString("objective")).build();
                	LOGGER.info("Obiettivo: {}", objective);
                	players.add(Player.builder()
                			.userName(username)
                			.gameId(gameId)
                			.territories(new ArrayList<Territory>())
                			.objective(objective)
                			.color(Player.PlayerColor.valueOf(color))
                		    .setUpCompleted(setUpCompleted).build());
                }
            }
        } catch (SQLException e) {throw new GameException("Errore durante il recupero degli utenti nel gioco.", e);
        }finally {
            closePreparedStatement(pstmt);
        }
        return players;
    }
    
    public List<Territory> getAllTerritories(String player) throws GameException {
		String sql = "SELECT * FROM territories WHERE player = ?";
		PreparedStatement pstmt = null;
		List<Territory> result = new ArrayList<>();
		try {
			pstmt = prepareStatement(sql);
			pstmt.setString(1, player);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String name = rs.getString("name");
					int continent = rs.getInt("continent");
					String idOwner = rs.getString("player");
					int armies = rs.getInt("armies");
					String svgPath = rs.getString("svgPath");
					Territory territory = Territory.builder().name(name).continent(continent).idOwner(idOwner).armies(armies).svgPath(svgPath).build();
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
	public Turn getLastTurnByGameId(String gameId) throws GameException {
	    String sql = "SELECT * FROM turns WHERE gameId = ? ORDER BY indexTurn DESC LIMIT 1";
	    PreparedStatement pstmt = null;
	    Turn turn = null;
		try {
			pstmt = prepareStatement(sql);
			pstmt.setString(1, gameId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int index = rs.getInt("index");
					String player = rs.getString("player");
					int numberOfTroops = rs.getInt("numberOfTroops");
					String attackerTerritory = rs.getString("attackerTerritory");
					String defenderTerritory = rs.getString("defenderTerritory");
					turn = Turn.builder().indexTurn(index)
							.player(Player.builder().userName(player).gameId(gameId).build())
							.numberOfTroops(numberOfTroops)
							.attackerTerritory(Territory.builder().name(attackerTerritory).build())
							.defenderTerritory(Territory.builder().name(defenderTerritory).build()).build();
				}
				return turn;
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante il recupero dell'ultimo turno.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
    
    @Override
	public List<TerritoryCard> getAllComboCards(String player, String gameId) throws GameException {
		String sql = "SELECT * FROM comboCards WHERE player = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		List<TerritoryCard> result = new ArrayList<>();
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, player);
			pstmt.setString(2, gameId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					TerritoryCard card = TerritoryCard.builder()
													  .territory(Territory.builder()
															  .name((rs.getString("territory"))).build())
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
    
    // --------------------------------------------------------
    // ------------------------ UPDATE ------------------------
    // --------------------------------------------------------
    private void executeUpdate(String sql, Object... params) throws GameException {
    	PreparedStatement pstmt = null;
        try {
        	pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            int updatedRows = pstmt.executeUpdate();
            if (updatedRows == 0) {
                LOGGER.error("Nessun elemento aggiornato: potrebbe non esistere un elemento con l'ID specificato.");
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
    
    public void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException {
        String sql = "UPDATE players SET setUpCompleted = ? WHERE username = ?";
        executeUpdate(sql, setUpCompleted, username);
    }

    public void updatePlayerObjective(String username, ICard objective) throws GameException {
        String sql = "UPDATE players SET objective = ? WHERE username = ?";
        executeUpdate(sql, ((ObjectiveCard)objective).getObjective(), username);
    }

    public void updatePlayerColor(Player player) throws GameException {
        String sql = "UPDATE players SET color = ? WHERE username = ?";
        executeUpdate(sql, player.getColor().name(), player.getUserName());
    }

    public void updateTerritoryOwner(String territoryName, Player player) throws GameException {
        String sql = "UPDATE territories SET player = ? WHERE name = ?";
        executeUpdate(sql, player.getUserName(), territoryName);
    }

    public void updateTerritoryArmies(String territoryName, int troops) throws GameException {
        String sql = "UPDATE territories SET armies = ? WHERE name = ?";
        executeUpdate(sql, troops, territoryName);
    }

    @Override
    public void updateTurnIndex(Turn turn, int index) throws GameException {
        String sql = "UPDATE turns SET indexTurn = ? WHERE player = ? AND gameId = ?";
        executeUpdate(sql, index, turn.getPlayer().getUserName(), turn.getPlayer().getGameId());
    }

    @Override
    public void updateOwner(TerritoryCard t, String player, String gameId) throws GameException {
        String sql = "UPDATE comboCards SET player = ? WHERE gameId = ? AND territory = ?";
        executeUpdate(sql, player, gameId, t.getTerritory().getName());
    }

    // --------------------------------------------------------
    // ------------------------ DELETE ------------------------
    // --------------------------------------------------------

    @Override
    public void deleteUser(User user) throws UserException {
        String sql = "DELETE FROM users WHERE username = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {throw new UserException("Errore durante l'eliminazione dell'utente.", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
	@Override
    public void deleteGame(IGame game) throws GameException {
        String sql = "DELETE FROM games WHERE gameId = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, game.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {throw new GameException("Errore durante l'eliminazione del gioco", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }

    public void deletePlayer(String username) throws GameException {
        String sql = "DELETE FROM players WHERE username = ?";
        PreparedStatement pstmt = null;
        try  {
        	pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {throw new GameException("Errore durante l'eliminazione del giocatore.", e);
        }finally {
            closePreparedStatement(pstmt);
        }
    }
    
    public void deleteTerritory(String name) throws GameException {
        String sql = "DELETE FROM territories WHERE name = ?";
        PreparedStatement pstmt = null;
        try {
        	pstmt = prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new GameException("Errore durante l'eliminazione del territorio.", e);
        }finally {
            closePreparedStatement(pstmt);
        }
    }
    
	@Override
	public void deleteTurn(Turn turn) throws GameException {
		String sql = "DELETE FROM turns WHERE indexTurn = ? AND player = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, turn.getIndexTurn());
			pstmt.setString(2, turn.getPlayer().getUserName());
			pstmt.setString(3, turn.getPlayer().getGameId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new GameException("Errore durante l'eliminazione del turno.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}

	@Override
	public void deleteComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
		String territoryComboCard = t.getTerritory().getName();
		String symbolComboCard = t.getSymbol().name();
		String sqlComboCard = "DELETE FROM comboCards WHERE player = ? AND gameId = ? AND territory = ? AND symbol = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sqlComboCard);
			pstmt.setString(1, owner.getUserName());
			pstmt.setString(2, gameId);
			pstmt.setString(3, territoryComboCard);
			pstmt.setString(4, symbolComboCard);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new GameException("Errore durante l'eliminazione della carta combo.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
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
			} catch (SQLException e) {throw new GameException("Errore durante il recupero di una partita", e);
			}
        return newGame;
	}
		
}