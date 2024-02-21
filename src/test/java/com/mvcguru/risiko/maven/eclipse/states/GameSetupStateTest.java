package com.mvcguru.risiko.maven.eclipse.states;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class GameSetupStateTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GameSetupStateTest.class);

	GameConfiguration config = GameConfiguration.builder()
								.mode(GameMode.EASY)
								.numberOfPlayers(2)
								.idMap("TestMap")
								.build(); 
	
    @Test
    void testOnActionSetup() throws FullGameException, GameException, DatabaseConnectionException, UserException, IOException {
    	IGame game = FactoryGame.getInstance().createGame(config);
    	
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
        	        
        IDeck objectiveDeck = game.getDeckObjective();
        IDeck terrirotyDeck = game.getDeckTerritory();
        
        
        
		LOGGER.info("Objective Deck: {}", objectiveDeck);
		LOGGER.info("Territory Deck: {}", terrirotyDeck);
		LOGGER.info("Players: {}", game.getPlayers());
		LOGGER.info("Database Players: {}", GameRepository.getInstance().getAllPlayers(game.getId()));
		
        
		GameRepository.getInstance().deleteGame(game);
		GameRepository.getInstance().removePlayer(player1.getUserName());
		GameRepository.getInstance().removePlayer(player2.getUserName());
    }
	
}