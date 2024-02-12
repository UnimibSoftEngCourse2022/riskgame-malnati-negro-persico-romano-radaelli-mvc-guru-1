package com.mvcguru.risiko.maven.eclipse.service.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.User;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

public class DaoSQLiteImpl implements UserDao,GameDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoSQLiteImpl.class);
    private Connection connection;
    private static DaoSQLiteImpl instance;
    
	public static synchronized DaoSQLiteImpl getInstance()
			throws DatabaseConnectionException, GameException, UserException {
		if (instance == null) {
			instance = new DaoSQLiteImpl(DatabaseConnection.getSqliteDbUrl());
		}
		return instance;
	}

    public DaoSQLiteImpl(String dbUrl) throws DatabaseConnectionException, UserException, GameException {
        try {
            connection = DriverManager.getConnection(dbUrl);
            createUsersTable();
            createGamesTable();
            if (connection.isClosed()) {
                throw new DatabaseConnectionException("Connessione al database non riuscita");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connessione al database non riuscita");
        }
    }


    private PreparedStatement prepareStatement(String sql, String... parameters) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setString(i + 1, parameters[i]);
            }
        } catch (SQLException e) {
            if (pstmt != null) {
                pstmt.close();
            }
            throw e;
        }
        return pstmt;
    }


    private void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {LOGGER.error("Error closing PreparedStatement", e);}
        }
    }
    
    //mi serve per i test
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Errore durante la chiusura della connessione al database", e);
            }
        }
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
        String sql = "CREATE TABLE IF NOT EXISTS games (\n"
                + "game_id INTEGER PRIMARY KEY,\n"
        		+ "mode TEXT NOT NULL,\n"
                + "number_of_players INTEGER NOT NULL,\n"
                + "idMap TEXT NOT NULL\n" 
                + ");";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareStatement(sql);
            pstmt.execute();
        } catch (SQLException e) {
        	throw new GameException("Errore durante la creazione della tabella partite.", e);
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
			System.out.println("SOno qua");
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
	public IGame getGameById(int gameId) throws GameException {
		
		   String sql = "SELECT * FROM games WHERE game_id = ?";
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    IGame game = null;

		    try {
		    	pstmt = prepareStatement(sql, String.valueOf(gameId));
		        rs = pstmt.executeQuery();

		        if (rs.next()) {
		            game = extractGameFromResultSet(rs);
		        }
		    } catch (SQLException e) {
		        throw new GameException("Errore durante il recupero del gioco con ID " + gameId, e);
		    }
		    return game;
	}


	@Override
	public void registerGame(IGame game) throws GameException {
        String sql = "INSERT INTO games (game_id, mode, number_of_players, idMap) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, game.getId());
            pstmt.setString(2, game.getConfiguration().getModeString());
            pstmt.setInt(3, game.getConfiguration().getNumberOfPlayers());
            pstmt.setString(4, game.getConfiguration().getIdMap());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new GameException("Errore durante la registrazione del gioco", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }

	@Override
    public void deleteGame(IGame game) throws GameException {
        String sql = "DELETE FROM games WHERE game_id = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, game.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new GameException("Errore durante l'eliminazione del gioco", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }

	@Override
    public List<IGame> getAllGames() throws GameException {
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
        } catch (SQLException e) {
            throw new GameException("Errore durante il recupero di tutte le partite", e);
        } finally {
            closePreparedStatement(pstmt);
        }
    }

	private IGame extractGameFromResultSet(ResultSet rs) throws GameException{
		IGame newGame = null;
		GameConfiguration config = GameConfiguration.builder().build();
        try {
        	config.setModeFromString(rs.getString("mode"));
        	config.setNumberOfPlayers(rs.getInt("number_of_players"));
	        config.setIdMap(rs.getString("idMap"));
	        newGame = FactoryGame.getInstance().creaPartita(config);
			} catch (SQLException e) {throw new GameException("Errore durante il recupero di una partita", e);
			}
        return newGame;
	}
}