package com.mvcguru.risiko.maven.eclipse.states;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryCardBody;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.GameExit;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.ComboRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.SetUpBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

class LobbyStateTest {
	Logger LOGGER = LoggerFactory.getLogger(LobbyStateTest.class);
	GameConfiguration config = GameConfiguration.builder()
								.mode(GameMode.HARD)
								.numberOfPlayers(2)
								.idMap("TestMap")
								.build(); 
	
	@Test
	void totalGameLogic() throws IOException, GameException, DatabaseConnectionException, UserException, FullGameException {
		
		// Create a game with the configuration
		//create LobbyState
		IGame game = FactoryGame.getInstance().createGame(config);
		
		assertEquals(game.getConfiguration(), config);
		assertNotNull(game.getConfiguration());
		assertNotNull(game.getContinents());
		assertNotNull(game.getDeckObjective());
		assertNotNull(game.getDeckTerritory());
		
		assertEquals(game.getState().getClass().toString(), LobbyState.class.toString());
		GameRepository.getInstance().insertGame(game);
		
		// Create a player and add it to the game
		Player player1 = Player.builder().userName("Bobby").gameId(game.getId()).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
		GameRepository.getInstance().addPlayer(player1);
		GameEntry gameEntry = GameEntry.builder().player(player1).build();
		game.getState().onActionPlayer(gameEntry);
		
		assertEquals(game.getPlayers().size(), 1);
		assertEquals(game.getPlayers().get(0).getUserName(), "Bobby");
		
		// Remove the player from the game
		GameExit exitState = GameExit.builder().player(player1).build();
		game.getState().onActionPlayer(exitState);
		
		assertEquals(game.getPlayers().size(), 0);
		
		// Add the player again
		game.getState().onActionPlayer(gameEntry);
		
		assertEquals(game.getPlayers().size(), 1);
		assertEquals(game.getPlayers().get(0).getUserName(), "Bobby");
		
		// Create a second player and add it to the game
        Player player2 = Player.builder().userName("Tommy").gameId(game.getId()).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
        GameRepository.getInstance().addPlayer(player2);
        
        GameEntry gameEntry2 = GameEntry.builder().player(player2).build();
        game.getState().onActionPlayer(gameEntry2);
        
        assertEquals(game.getPlayers().size(), 2);
        assertEquals(game.getPlayers().get(1).getUserName(), "Tommy");
        
        // SetupState
        assertEquals(game.getState().getClass().toString(), GameSetupState.class.toString());
        
        LOGGER.info("GameSetupState {}" , player1.getObjective().getObjective());
        LOGGER.info("GameSetupState {}" , player2.getObjective().getObjective());
        
        List<Territory> territories = player1.getTerritories();
        List<TerritoryBody> territoryBodies = new ArrayList<>();
        TerritoryBody territoryBody = null;
		for (Territory territory : territories) {
			territoryBody = TerritoryBody.builder().name(territory.getName()).troops(2).build();
			territoryBodies.add(territoryBody);
		}
        
		SetUpBody setUpBody = SetUpBody.builder().territories(territoryBodies).build();
		TerritorySetup territorySetup = TerritorySetup.builder().player(player1).setUpBody(setUpBody).build();
		game.getState().onActionPlayer(territorySetup);
		
		for (Territory territory : territories) {
			assertEquals(territory.getArmies(), 2);
		}
		
		List<Territory> territories2 = player2.getTerritories();
		for (Territory territory : territories2) {
			assertEquals(territory.getArmies(), 0);
		}
		List<TerritoryBody> territoryBodies2 = new ArrayList<>();
		TerritoryBody territoryBody2 = null;
		for (Territory territory : territories2) {
			territoryBody2 = TerritoryBody.builder().name(territory.getName()).troops(2).build();
			territoryBodies2.add(territoryBody2);
		}
		
		LOGGER.info("Player2: {}", GameRepository.getInstance().getPlayer(player2.getUserName(), game.getId()));
		
		SetUpBody setUpBody2 = SetUpBody.builder().territories(territoryBodies2).build();
		TerritorySetup territorySetup2 = TerritorySetup.builder().player(player2).setUpBody(setUpBody2).build();
		game.getState().onActionPlayer(territorySetup2);
		
		for (Territory territory : territories2) {
			assertEquals(territory.getArmies(), 2);
		}
		
		assertEquals(game.getState().getClass().toString(), StartTurnState.class.toString());
		
		// StartTurnState
		
		TerritoryCardBody territoryCardBody1 = TerritoryCardBody.builder().name(territories.get(0).getName()).symbol(CardSymbol.ARTILLERY.toString()).build();
		TerritoryCardBody territoryCardBody2 = TerritoryCardBody.builder().name(territories.get(1).getName()).symbol(CardSymbol.ARTILLERY.toString()).build();
		TerritoryCardBody territoryCardBody3 = TerritoryCardBody.builder().name(territories.get(2).getName()).symbol(CardSymbol.ARTILLERY.toString()).build();
		
		Territory t1 = GameRepository.getInstance().getTerritory(territoryCardBody1.getName(), player1.getUserName(), game.getId());
		Territory t2 = GameRepository.getInstance().getTerritory(territoryCardBody2.getName(), player1.getUserName(), game.getId());
		Territory t3 = GameRepository.getInstance().getTerritory(territoryCardBody3.getName(), player1.getUserName(), game.getId());
		
		TerritoryCard territoryCard = TerritoryCard.builder().territory(t1).symbol(CardSymbol.ARTILLERY).build();
		TerritoryCard territoryCard2 = TerritoryCard.builder().territory(t2).symbol(CardSymbol.ARTILLERY).build();
		TerritoryCard territoryCard3 = TerritoryCard.builder().territory(t3).symbol(CardSymbol.ARTILLERY).build();
		
		
		
		GameRepository.getInstance().insertComboCard(territoryCard, player1, game.getId());
		GameRepository.getInstance().insertComboCard(territoryCard2, player1, game.getId());
		GameRepository.getInstance().insertComboCard(territoryCard3, player1, game.getId());
		
		List<TerritoryCard> territoryCardList = new ArrayList<>();
		
		territoryCardList.add(territoryCard);
		territoryCardList.add(territoryCard2);
		territoryCardList.add(territoryCard3);
		
		List<TerritoryCardBody> territoryCardBodies = new ArrayList<>();
		territoryCardBodies.add(territoryCardBody1);
		territoryCardBodies.add(territoryCardBody2);
		territoryCardBodies.add(territoryCardBody3);
		
		player1.setComboCards(territoryCardList);
		
		ComboRequestBody comboRequestBody = ComboRequestBody.builder().username(player1.getUserName()).comboCards(territoryCardBodies).build();
		ComboRequest comboRequest = ComboRequest.builder().player(player1).comboRequestBody(comboRequestBody).build();
		game.getState().onActionPlayer(comboRequest);
		
		assertTrue(player1.getComboCards().size() == 0);
		
		
        List<TerritoryBody> territoryBodies3 = new ArrayList<>();
        TerritoryBody territoryBody3 = null;
		
        for (int i = 0; i < 4; i++) {
        	territoryBody3 = TerritoryBody.builder().name(territories.get(i).getName()).troops(1).build();
        	territoryBodies3.add(territoryBody3);
        }
        
		SetUpBody setUpBody3 = SetUpBody.builder().territories(territoryBodies3).build();
		TerritorySetup territorySetup3 = TerritorySetup.builder().player(player1).setUpBody(setUpBody3).build();
		game.getState().onActionPlayer(territorySetup3);
		
		
	}
	
	
}
