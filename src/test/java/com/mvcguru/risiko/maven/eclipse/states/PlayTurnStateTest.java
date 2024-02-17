package com.mvcguru.risiko.maven.eclipse.states;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.controller.bodyRequest.ComboRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.bodyRequest.SetUpBody;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.Continent;

import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.FactoryGame;

import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;

class PlayTurnStateTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameSetupState.class);

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
		
		System.out.println(player1.getColor() + " \n" + player1.getTerritories() + "\n " + player1.getObjective());
		
		SetUpBody setUpBody1 = SetUpBody.builder().username(player1.getUserName()).territories(player1.getTerritories()).build();
		TerritorySetup territorySetup = TerritorySetup.builder().player(player1).setUpBody(setUpBody1).build();
		game.getState().onActionPlayer(territorySetup);
		
		
		
		SetUpBody setUpBody2 = SetUpBody.builder().username(player2.getUserName()).territories(player2.getTerritories()).build();
		TerritorySetup territorySetup2 = TerritorySetup.builder().player(player2).setUpBody(setUpBody2).build();
		game.getState().onActionPlayer(territorySetup2);
		assertEquals(game.getState().getClass(), PlayTurnState.class);
		
		
		List<TerritoryCard> comboCards = new ArrayList<>();
		
		ICard c1 = TerritoryCard.builder().territory(Territory.builder().name("Cina").build()).symbol(CardSymbol.JOLLY).build();
		ICard c2 = TerritoryCard.builder().territory(Territory.builder().name("Egitto").build()).symbol(CardSymbol.ARTILLERY).build();
		ICard c3 = TerritoryCard.builder().territory(Territory.builder().name("Brasile").build()).symbol(CardSymbol.JOLLY).build();
		
		comboCards.add((TerritoryCard) c1);
		comboCards.add((TerritoryCard) c2);
		comboCards.add((TerritoryCard) c3);
		
		ComboRequestBody comboRequestBody = ComboRequestBody.builder().username(player1.getUserName()).comboCards(comboCards).build();
		ComboRequest comboRequest = ComboRequest.builder().player(player1).comboRequestBody(comboRequestBody).build();
		
		game.getState().onActionPlayer(comboRequest);
		
		List<Territory> list = new ArrayList<>();
		list.add(Territory.builder().name("Brasile").build());
		list.add(Territory.builder().name("Argentina").build());
		list.add(Territory.builder().name("Perù").build());
		list.add(Territory.builder().name("Venezuela").build());
		game.getCurrentTurn().continentCheck(list);
		
    }
}
