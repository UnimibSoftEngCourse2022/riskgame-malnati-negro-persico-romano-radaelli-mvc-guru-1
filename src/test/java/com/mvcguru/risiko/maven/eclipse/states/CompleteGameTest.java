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

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.DefenceRequest;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.GameExit;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.actions.TurnSetUp;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.AttackRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.BattleBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.ComboRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.DefenceRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.SetUpBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Turn;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

class CompleteGameTest {
	Logger LOGGER = LoggerFactory.getLogger(CompleteGameTest.class);
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
		
		assertEquals(1, game.getPlayers().size());
		assertEquals("Bobby", game.getPlayers().get(0).getUserName());
		
		// Remove the player from the game
		GameExit exitState = GameExit.builder().player(player1).build();
		game.getState().onActionPlayer(exitState);
		
		assertEquals(0, game.getPlayers().size());
		
		// Add the player again
		game.getState().onActionPlayer(gameEntry);
		
		assertEquals(1, game.getPlayers().size());
		assertEquals("Bobby", game.getPlayers().get(0).getUserName());
		
		// Create a second player and add it to the game
        Player player2 = Player.builder().userName("Tommy").gameId(game.getId()).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
        GameRepository.getInstance().addPlayer(player2);
        
        GameEntry gameEntry2 = GameEntry.builder().player(player2).build();
        game.getState().onActionPlayer(gameEntry2);
        
        assertEquals(2, game.getPlayers().size());
        assertEquals("Tommy", game.getPlayers().get(1).getUserName());
        
        // SetupState
        assertEquals(game.getState().getClass().toString(), GameSetupState.class.toString());
        
        LOGGER.info("GameSetupState {}" , player1.getObjective().getObjective());
        LOGGER.info("GameSetupState {}" , player2.getObjective().getObjective());
        
        List<Territory> territories = player1.getTerritories();
        List<TerritoryBody> territoryBodies = new ArrayList<>();
        TerritoryBody territoryBody = null;
		for (Territory territory : territories) {
			territoryBody = TerritoryBody.builder().name(territory.getName()).troops(5).build();
			territoryBodies.add(territoryBody);
		}
        
		SetUpBody setUpBody = SetUpBody.builder().territories(territoryBodies).build();
		TerritorySetup territorySetup = TerritorySetup.builder().player(player1).setUpBody(setUpBody).build();
		game.getState().onActionPlayer(territorySetup);
		
		for (Territory territory : territories) {
			assertEquals(5, territory.getArmies());
		}
		
		List<Territory> territories2 = player2.getTerritories();
		for (Territory territory : territories2) {
			assertEquals(0, territory.getArmies());
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
			assertEquals(2,territory.getArmies());
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
		
		assertEquals(0, player1.getComboCards().size());
		
        List<TerritoryBody> territoryBodies3 = new ArrayList<>();
        TerritoryBody territoryBody3 = null;
		
        for (int i = 0; i < 4; i++) {
        	territoryBody3 = TerritoryBody.builder().name(territories.get(i).getName()).troops(1).build();
        	territoryBodies3.add(territoryBody3);
        }
        
		SetUpBody setUpBody3 = SetUpBody.builder().territories(territoryBodies3).build();
		TurnSetUp territorySetup3 = TurnSetUp.builder().player(player1).setUpBody(setUpBody3).build();
		game.getState().onActionPlayer(territorySetup3);

		assertEquals(game.getState().getClass().toString(), BattleState.class.toString());

		
        // BattleState
		BattleBody  battleBodyAttacker = BattleBody.builder().nameTerritory("Brasile").username(player1.getUserName()).build();
		BattleBody  battleBody2Difender = BattleBody.builder().nameTerritory("Argentina").username(player2.getUserName()).build();
		
		AttackRequestBody attackRequestBody = AttackRequestBody.builder().attackerTerritory(battleBodyAttacker).defenderTerritory(battleBody2Difender).numAttDice(3).build();
		AttackRequest attackRequest = AttackRequest.builder().player(player1).requestAttackBody(attackRequestBody).build();
		
	    game.getState().onActionPlayer(attackRequest);
		
		assertEquals(game.getState().getClass().toString(), BattleState.class.toString());
		
		DefenceRequestBody defenceRequestBody = DefenceRequestBody.builder().username(player2.getUserName()).numDefDice(2).build();
		
		DefenceRequest defenceRequest = DefenceRequest.builder().defenderRequestBody(defenceRequestBody).build();
		
		game.getState().onActionPlayer(defenceRequest);
	}
}
