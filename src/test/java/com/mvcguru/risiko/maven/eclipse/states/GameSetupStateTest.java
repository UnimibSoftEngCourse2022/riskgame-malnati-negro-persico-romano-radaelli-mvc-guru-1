package com.mvcguru.risiko.maven.eclipse.states;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.controller.GameController;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameSetupStateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private GameSetupState gameSetupState;

    @Test
    void assignAllTest() throws IOException, FullGameException {
    	GameConfiguration config = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(2)
                .idMap("TestMap")
                .build();
    	
    	IGame game = FactoryGame.getInstance().createGame(config);
    	
		Player player1 = Player.builder().userName("Bobby").gameId(game.getId()).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
		Player player2 = Player.builder().userName("Tommy").gameId(game.getId()).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
		
		LOGGER.info("Player: {}", player1);
		LOGGER.info("Player: {}", player2);
    	
    	GameEntry gameEntry = GameEntry.builder().player(player1).build();
        game.getState().onActionPlayer(gameEntry);
        
    	LOGGER.info(game.getState().getClass().toString());

        
    	GameEntry gameEntry2 = GameEntry.builder().player(player2).build();
        game.getState().onActionPlayer(gameEntry2);
    	
    	
		for (Player player : game.getPlayers()) {
			assertNotNull(player.getColor());
			LOGGER.info("Player: {}", player.getColor());
			assertFalse(player.getTerritories().isEmpty());
			LOGGER.info("Player: {}", player.getTerritories());
			assertNotNull(player.getObjective());
			LOGGER.info("Player: {}", player.getObjective());
		}
		
    }
}