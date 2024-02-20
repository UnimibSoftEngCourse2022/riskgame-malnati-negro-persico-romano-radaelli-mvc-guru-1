package com.mvcguru.risiko.maven.eclipse.states;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

class LobbyStateTest {

	GameConfiguration config = GameConfiguration.builder()
								.mode(GameMode.EASY)
								.numberOfPlayers(2)
								.idMap("TestMap")
								.build(); 
	
    @Test
    void testOnActionPlayerGameEntry() throws FullGameException, GameException, DatabaseConnectionException, UserException, IOException {
    	IGame game = FactoryGame.getInstance().createGame(config);
    	String gameId = game.getId();
    	
        Player player1 = Player.builder().userName("Bobby").gameId(game.getId()).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
        Player player2 = Player.builder().userName("Tommy").gameId(game.getId()).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
        
        assertEquals(game.getState().getClass().toString(), LobbyState.class.toString());
        
        GameEntry gameEntry = GameEntry.builder().player(player1).build();
        game.getState().onActionPlayer(gameEntry);
        
        GameRepository.getInstance().addPlayer(player1);
        
        GameEntry gameEntry2 = GameEntry.builder().player(player2).build();
        game.getState().onActionPlayer(gameEntry2);
        
        GameRepository.getInstance().addPlayer(player2);
        
        assertEquals(game.getState().getClass().toString(), GameSetupState.class.toString());

        assertEquals(game.getPlayers().size(), game.getConfiguration().getNumberOfPlayers());
        
        GameRepository.getInstance().deleteGame(game);
        GameRepository.getInstance().insertGame(game);
        GameRepository.getInstance().deleteGame(game);
        GameRepository.getInstance().insertGame(game);
        GameRepository.getInstance().deleteGame(game);
        GameRepository.getInstance().insertGame(game);
        GameRepository.getInstance().deleteGame(game);
        GameRepository.getInstance().insertGame(game);
        GameRepository.getInstance().deleteGame(game);
        
        GameRepository.getInstance().removePlayer(player1.getUserName());
        GameRepository.getInstance().removePlayer(player2.getUserName());
        
        assertTrue(true, "Test passed");
        
    }
	
}
