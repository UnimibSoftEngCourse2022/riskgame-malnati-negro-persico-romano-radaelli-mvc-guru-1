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
    private Connection connection;
    private static DaoSQLiteImpl instance;
    

    public DaoSQLiteImpl(String dbUrl) throws DatabaseConnectionException, UserException, GameException {
        try {
            connection = DriverManager.getConnection(dbUrl);
            createUsersTable();
            createGamesTable();
            createPlayerTable();
            createTerritoryTable(); 
            createTurnTable();
            createComboCardsTable();
            if (connection.isClosed()) {
                throw new DatabaseConnectionException("Connessione al database non riuscita");
            }
        } catch (SQLException e) {
            LOGGER.error("Errore durante la connessione al database", e);
        }
    }

    public static synchronized DaoSQLiteImpl getInstance() throws DatabaseConnectionException, UserException, GameException {
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
    
    @Override
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

    public void createUsersTable() throws UserException {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "username text PRIMARY KEY,\n"
                + "password text NOT NULL\n"
                + ");";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {throw new UserException("Errore durante la creazione della tabella users.", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
	@Override
	public void createGamesTable() throws GameException {
		String sql = "CREATE TABLE IF NOT EXISTS games (\n" +
	             "gameId TEXT PRIMARY KEY,\n" +
	             "mode TEXT NOT NULL,\n" +
	             "number_of_players INTEGER NOT NULL,\n" +
	             "idMap TEXT NOT NULL,\n" +
	             "state TEXT NOT NULL\n" +
	             ");";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {throw new GameException("Errore durante la creazione della tabella partite.", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }

    @Override
    public void createPlayerTable() throws GameException {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                     "username TEXT," +
                     "gameId TEXT," +
                     "color TEXT," +
                     "objective TEXT,\n" +
		             "setUpCompleted BOOLEAN NOT NULL,\n" +
                     "FOREIGN KEY(gameId) REFERENCES games(gameId)," +
                     "PRIMARY KEY (username)" +
                     ");";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {throw new GameException("Errore durante la creazione della tabella players.", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
    @Override
    public void createTerritoryTable() throws GameException {
        String sql = "CREATE TABLE IF NOT EXISTS territories (" +
                     "name TEXT," +
                     "gameId TEXT," +
                     "player TEXT," +
                     "continent INTEGER," +
                     "armies INTEGER," +
                     "svgPath TEXT," +
                     "FOREIGN KEY(player) REFERENCES players(username)," +
                     "FOREIGN KEY(gameId) REFERENCES games(gameId)," +
                     "PRIMARY KEY (name, player, gameId)" +
                     ");";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {throw new GameException("Errore durante la creazione della tabella territories.", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
    @Override
    public void createTurnTable() throws GameException {
        String sql = "CREATE TABLE IF NOT EXISTS turns (" +
                     "indexTurn INTEGER," +
                     "player TEXT," +
                     "gameId TEXT," +
                     "numberOfTroops INTEGER," +
                     "attackerTerritory TEXT," +
                     "defenderTerritory TEXT," +
                     "FOREIGN KEY(player) REFERENCES players(username)," +
                     "FOREIGN KEY(gameId) REFERENCES games(gameId)," +
                     "PRIMARY KEY (indexTurn, gameId)" +
                     ");";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {throw new GameException("Errore durante la creazione della tabella turns.", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
    
    @Override
	public void createComboCardsTable() throws GameException {
		String sql = "CREATE TABLE IF NOT EXISTS comboCards (" +
					 "player TEXT," + 
					 "gameId TEXT," + 
					 "territory TEXT," + 
					 "symbol TEXT," + 
					 "FOREIGN KEY(player) REFERENCES players(username)," + 
					 "FOREIGN KEY(gameId) REFERENCES games(gameId)," + 
					 "FOREIGN KEY(territory) REFERENCES territories(name)," +
					 "PRIMARY KEY (player, gameId, territory, symbol)" + 
					 ");";
		PreparedStatement pstmt = null;
		try {
			pstmt = prepareStatement(sql);
			pstmt.execute();
		} catch (SQLException e) {
			throw new GameException("Errore durante la creazione della tabella comboCards.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
	//UserDao methods
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
    public void registerUser(User user) throws UserException {
    	if (user == null) {
            throw new UserException("L'utente non può essere null", new SQLException());
        }
		if (getUserByUsernameAndPassword(user.getUsername(), user.getPassword()) != null) {
			throw new UserException("L'utente esiste già", new SQLException());
		}
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql, user.getUsername(), user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {throw new UserException("Errore durante la registrazione dell'utente.", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }

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
    
    //GameDao methods
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
	public void registerGame(IGame game) throws GameException {
		String gameId = game.getId();
		String mode = game.getConfiguration().getMode().name();
		int numberOfPlayers = game.getConfiguration().getNumberOfPlayers();
		String idMap = game.getConfiguration().getIdMap();
		String state = game.getState().getClass().getSimpleName();		
        String sql = "INSERT INTO games (gameId, mode, number_of_players, idMap, state) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, gameId);
            pstmt.setString(2, mode);
            pstmt.setInt(3, numberOfPlayers);
            pstmt.setString(4, idMap);
            pstmt.setString(5, state);
            pstmt.executeUpdate();
        } catch (SQLException e) {throw new GameException("Errore durante la registrazione del gioco", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }
	
	@Override
	public void updateState(String gameId, GameState newState) throws GameException {
	    String sql = "UPDATE games SET state = ? WHERE gameId = ?";
	    PreparedStatement pstmt = null;
	    try {
	        pstmt = connection.prepareStatement(sql);
	        pstmt.setString(1, newState.getClass().getSimpleName());
	        pstmt.setString(2, gameId);
	        int updatedRows = pstmt.executeUpdate();
	        if (updatedRows == 0) {
	            LOGGER.error("Nessuna partita aggiornata: potrebbe non esistere una partita con l'ID specificato.");
	        }
	    } catch (SQLException e) {
	        throw new GameException("Errore durante l'aggiornamento dello stato del gioco", e);
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
            default:
            	LOGGER.error("Stato non riconosciuto");
                break;
            }
			} catch (SQLException e) {throw new GameException("Errore durante il recupero di una partita", e);
			}
        return newGame;
	}
	
	//PlayerDao methods
	public void insertPlayer(Player player) throws GameException {
		String username = player.getUserName();
		String gameId = player.getGameId();
		String color = player.getColor().name();
		//String objective = ((ObjectiveCard)player.getObjective()).getObjective();
		boolean setUpCompleted = player.isSetUpCompleted();
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO players (username, gameId, color, setUpCompleted) VALUES (?, ?, ?, ?)";
        try{
        	pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, gameId);
            pstmt.setString(3, color);
            pstmt.setBoolean(4, setUpCompleted);
            pstmt.executeUpdate();
        } catch (SQLException e) {throw new GameException("Errore durante l'inserimento del giocatore.", e);
        }finally {
            closePreparedStatement(pstmt);
        }
    }
	
	public void updateSetUpCompleted(String username, boolean setUpCompleted) throws GameException {
		String sql = "UPDATE players SET setUpCompleted = ? WHERE username = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setBoolean(1, setUpCompleted);
			pstmt.setString(2, username);
			int updatedRows = pstmt.executeUpdate();
			if (updatedRows == 0) {
				LOGGER.error("Nessun giocatore aggiornato: potrebbe non esistere un giocatore con lo username specificato.");
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento del setup completato del giocatore.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
	public void updatePlayerObjective(String username, ICard objective) throws GameException {
		String sql = "UPDATE players SET objective = ? WHERE username = ?";
		PreparedStatement pstmt = null;
		try {
			LOGGER.info(((ObjectiveCard)objective).getObjective());
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, ((ObjectiveCard)objective).getObjective());
			pstmt.setString(2, username);
			int updatedRows = pstmt.executeUpdate();
			if (updatedRows == 0) {
				LOGGER.error(
						"Nessun giocatore aggiornato: potrebbe non esistere un giocatore con lo username specificato.");
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento dell'obiettivo del giocatore.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
	public void updatePlayerColor(String username, Player.PlayerColor color) throws GameException {
		String sql = "UPDATE players SET color = ? WHERE username = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, color.name());
			pstmt.setString(2, username);
			int updatedRows = pstmt.executeUpdate();
			if (updatedRows == 0) {
				LOGGER.error("Nessun giocatore aggiornato: potrebbe non esistere un giocatore con lo username specificato.");
						}
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento del colore del giocatore.", e);
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
    
    //TerritoryDao methods
    public void insertTerritory(Territory territory, String gameId) throws GameException {
    	String name = territory.getName();
    	String player = territory.getIdOwner();
    	int continent = territory.getContinent();
    	int armies = territory.getArmies();
    	String svgPath = territory.getSvgPath();
    	
        String sql = "INSERT INTO territories (name, player, gameId, continent, armies, svgPath) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        try {
        	pstmt = prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, player);
            pstmt.setString(3, gameId);
            pstmt.setInt(4, continent);
            pstmt.setInt(5, armies);
            pstmt.setString(6, svgPath);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new GameException("Errore durante l'inserimento del territorio.", e);
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
    
    public void updateTerritoryOwner(String territoryName, Player player) throws GameException {
    	String username = player.getUserName();
    	String sql = "UPDATE territories SET player = ? WHERE name = ?";
    	PreparedStatement pstmt = null;
    	try{
    		pstmt = prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, territoryName);
            
            int updatedRows = pstmt.executeUpdate();
            if (updatedRows == 0) {
                LOGGER.error("Nessun territorio aggiornato: potrebbe non esistere un territorio con il nome specificato.");
            }
        } catch (SQLException e) {
            throw new GameException("Errore durante l'aggiornamento del proprietario del territorio.", e);
        }finally {
            closePreparedStatement(pstmt);
        }
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
    
	public void updateTerritoryArmies(String territoryName, int troops) throws GameException {
		String sql = "UPDATE territories SET armies = ? WHERE name = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = prepareStatement(sql);
			pstmt.setInt(1, troops);
			pstmt.setString(2, territoryName);
			int updatedRows = pstmt.executeUpdate();
			if (updatedRows == 0) {
				LOGGER.error("Nessun territorio aggiornato: potrebbe non esistere un territorio con il nome specificato.");
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento delle truppe del territorio.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}	
	
	//TurnDao methods
	public void insertTurn(Turn turn) throws GameException {
		int index = turn.getIndexTurn();
		String player = turn.getPlayer().getUserName();
		String gameId = turn.getPlayer().getGameId();
		int numberOfTroops = turn.getNumberOfTroops();
		String attackerTerritory = turn.getAttackerTerritory().getName();
		String defenderTerritory = turn.getDefenderTerritory().getName();
		String sql = "INSERT INTO turns (indexTurn, player, gameId, numberOfTroops, attackerTerritory, defenderTerritory) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, index);
			pstmt.setString(2, player);
			pstmt.setString(3, gameId);
			pstmt.setInt(4, numberOfTroops);
			pstmt.setString(5, attackerTerritory);
			pstmt.setString(6, defenderTerritory);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new GameException("Errore durante l'inserimento del turno.", e);
		} finally {
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
	public void updateTurnIndex(Turn turn, int index) throws GameException {
		String sql = "UPDATE turns SET indexTurn = ? WHERE player = ? AND gameId = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, index);
			pstmt.setString(2, turn.getPlayer().getUserName());
			pstmt.setString(3, turn.getPlayer().getGameId());
			int updatedRows = pstmt.executeUpdate();
			if (updatedRows == 0) {
				LOGGER.error("Nessun turno aggiornato: potrebbe non esistere un turno con l'indice specificato.");
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento dell'indice del turno.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
	//ComboCardDao methods
	@Override
	public void insertComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
		String territory = t.getTerritory().getName();
		String symbol = t.getSymbol().name();
		String sql = "INSERT INTO comboCards (player, gameId, territory, symbol) VALUES (?, ?, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, owner.getUserName());
			pstmt.setString(2, gameId);
			pstmt.setString(3, territory);
			pstmt.setString(4, symbol);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new GameException("Errore durante l'inserimento della carta combo.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
	@Override
	public void deleteComboCard(TerritoryCard t, Player owner, String gameId) throws GameException {
		String territory = t.getTerritory().getName();
		String symbol = t.getSymbol().name();
		String sql = "DELETE FROM comboCards WHERE player = ? AND gameId = ? AND territory = ? AND symbol = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, owner.getUserName());
			pstmt.setString(2, gameId);
			pstmt.setString(3, territory);
			pstmt.setString(4, symbol);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new GameException("Errore durante l'eliminazione della carta combo.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
	@Override
	public void updateOwner(TerritoryCard t, String player, String gameId) throws GameException {
		String territory = t.getTerritory().getName();
		String sql = "UPDATE comboCards SET player = ? WHERE gameId = ? AND territory = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, player);
			pstmt.setString(2, gameId);
			pstmt.setString(3, territory);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento del proprietario della carta combo.", e);
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
	
}