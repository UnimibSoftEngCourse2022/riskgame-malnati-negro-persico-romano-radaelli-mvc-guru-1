package com.mvcguru.risiko.maven.eclipse.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class GameSetupState extends GameState {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GameSetupState.class);


	@Override
	public void onActionPlayer(TerritorySetup action) {
		//action.getPlayer().setTerritories(action.getSetUpBody().getTerritories());
		action.getPlayer().setSetUpCompleted(true);
		for (Player player : game.getPlayers()) {
			if (!player.isSetUpCompleted()) {
				return;
			}
		}
		game.setState(PlayTurnState.builder().game(game).build());
		game.startGame();
	}
	
	@Override
	public void setUpGame() throws GameException, DatabaseConnectionException, UserException {
		assignColor(game.getPlayers());
		assignTerritories(game.getDeckTerritory());
		assignObjective(game.getDeckObjective());
		ICard cardJolly1 = TerritoryCard.builder().territory(null).symbol(CardSymbol.JOLLY).build();
		ICard cardJolly2 = TerritoryCard.builder().territory(null).symbol(CardSymbol.JOLLY).build();
		game.getDeckTerritory().insertCard(cardJolly1);
		game.getDeckTerritory().insertCard(cardJolly2);
	}

	private void assignObjective(IDeck deckObjective) {
		deckObjective.shuffle();
		ICard card = null;
		
		for (Player player : game.getPlayers()) {
			card = deckObjective.drawCard();
			player.setObjective(card);
	        if(game.getConfiguration().getMode().name().equals("EASY")){
	        	deckObjective.insertCard(card);
	        }
		}
	}
    
	private void assignTerritories(IDeck deckTerritory) throws GameException, DatabaseConnectionException, UserException {
	    deckTerritory.shuffle();
	    int playerIndex = 0;
	    TerritoryCard card = (TerritoryCard)deckTerritory.drawCard(); 
	    
	    while (card != null) {
	    	game.getPlayers().get(playerIndex % game.getPlayers().size()).getTerritories().add(card.getTerritory());
	    	card.getTerritory().setOwner(game.getPlayers().get(playerIndex % game.getPlayers().size()));
	    	GameRepository.getInstance().insertTerritory(card.getTerritory());
	        playerIndex++;
	        card = (TerritoryCard) deckTerritory.drawCard(); 
	    }
	}


	private void assignColor(List<Player> players) {
		List<PlayerColor> colors = new ArrayList<>(Arrays.asList(PlayerColor.values()));
		colors.remove(PlayerColor.GREY);
		Collections.shuffle(colors);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setColor(colors.get(i));
		}
		
	}


}
