package com.mvcguru.risiko.maven.eclipse.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.controller.GameController;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class GameSetupState extends GameState {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    
	@Override
	public void onActionPlayer(TerritorySetup action) {
		//action.getPlayer().setSetupCompleted(true);
	}
	
	@Override
	public void setUpGame() {
		LOGGER.info("GameSetupState: inizio setup partita");
		assignColor(game.getPlayers());
		LOGGER.info("GameSetupState: assegnamento colori completato");
		assignTerritories(game.getDeckTerritory());
		LOGGER.info("GameSetupState: assegnamento territori completato");
		assignObjective(game.getDeckObjective());
		LOGGER.info("GameSetupState: assegnamento obiettivi completato");
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
    
	private void assignTerritories(IDeck deckTerritory) {
	    deckTerritory.shuffle();
	    int playerIndex = 0;
	    TerritoryCard card = (TerritoryCard)deckTerritory.drawCard(); 
	    
	    while (card != null) {
	    	game.getPlayers().get(playerIndex % game.getPlayers().size()).getTerritories().add(card.getTerritory());
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
