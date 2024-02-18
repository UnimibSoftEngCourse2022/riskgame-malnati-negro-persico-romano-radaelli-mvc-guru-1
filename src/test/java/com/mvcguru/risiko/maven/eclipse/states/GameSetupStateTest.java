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
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSetupStateTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameSetupState.class);

    @Test
    void assignAllTest() throws IOException, FullGameException, GameException, DatabaseConnectionException, UserException {
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
			assertFalse(player.getTerritories().isEmpty());
			assertNotNull(player.getObjective());
		}
		
		List<Continent> continents = game.parsingContinent();
		
		for (Continent continent : continents) {
			assertNotNull(continent.getContinentId());
			LOGGER.info("Continent intId: {}", continent.getContinentId());
			assertNotNull(continent.getName());
			LOGGER.info("Continent name: {}", continent.getName());
			assertFalse(continent.getTerritories().isEmpty());
			LOGGER.info("Continent territories: {}", continent.getTerritories());
		}
		
		List<Territory> lista = continents.get(0).getTerritories();
		for (Territory territory : lista) {
			LOGGER.info("Territory: {}", territory);
		}
		
		List<Territory> lista2 = player1.getTerritories();
		for (Territory territory : lista2) {
			LOGGER.info("Territory: {}", territory);
        }
		
    }
}