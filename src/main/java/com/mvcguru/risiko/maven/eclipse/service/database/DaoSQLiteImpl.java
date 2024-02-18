	package com.mvcguru.risiko.maven.eclipse.service.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import lombok.experimental.SuperBuilder;
import com.mvcguru.risiko.maven.eclipse.model.Territory;

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
			LOGGER.debug("Sono qua");
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
		    } catch (SQLException e) {throw new GameException("Errore durante il recupero del gioco con ID " + gameId, e);
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
		LOGGER.info("Stato: " + game.getState().getClass().getSimpleName());
		
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
        LOGGER.info("prima di try");
        try {
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();
            LOGGER.info("Dopo query");
            while (rs.next()) {
            	LOGGER.info("Prima extract");
                IGame game = extractGameFromResultSet(rs);
                LOGGER.info("partita: " + game.toString());
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
	        LOGGER.info("Dopo factory" );
	        newGame.setId(rs.getString("gameId"));
	        String stateName = rs.getString("state");
	        LOGGER.info("Stato: " + stateName);
            switch (stateName) {
            case "GameSetupState":
                newGame.setState(GameSetupState.builder().game(newGame).build());
                break;
            case "PlayTurnState":
                newGame.setState(PlayTurnState.builder().game(newGame).build());
                break;
            case "LobbyState":
                newGame.setState(LobbyState.builder().game(newGame).build());
                break;
            default:
            	LOGGER.error("Stato non riconosciuto");
                break;
            }
            LOGGER.info("Stato Aggiornato {}", newGame.getState());
			} catch (SQLException e) {throw new GameException("Errore durante il recupero di una partita", e);
			}
        return newGame;
	}
	
	//PlayerDao methods
	public void insertPlayer(Player player) throws GameException {
		String username = player.getUserName();
		LOGGER.info("Username: " + username);
		String gameId = player.getGameId();
		LOGGER.info("GameId: " + gameId);
		String color = player.getColor().name();
		LOGGER.info("Colore: " + color);
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
				LOGGER.error(
						"Nessun giocatore aggiornato: potrebbe non esistere un giocatore con lo username specificato.");
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento del setup completato del giocatore.", e);
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
        String sql = "SELECT username, gameId, color, setUpCompleted FROM players WHERE gameId = ?";
        PreparedStatement pstmt = null;
        try{
        	pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, gameId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                	String username = rs.getString("username");
                	String color = rs.getString("color");
                	boolean setUpCompleted = rs.getBoolean("setUpCompleted");
                	players.add(Player.builder()
                			.userName(username)
                			.gameId(gameId)
                			.territories(new ArrayList<Territory>())
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
					int armies = rs.getInt("armies");
					String svgPath = rs.getString("svgPath");
					Territory territory = Territory.builder().name(name).continent(continent).armies(armies).svgPath(svgPath).build();
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
				LOGGER.error(
						"Nessun territorio aggiornato: potrebbe non esistere un territorio con il nome specificato.");
			}
		} catch (SQLException e) {
			throw new GameException("Errore durante l'aggiornamento delle truppe del territorio.", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}	
	
}