package com.mvcguru.risiko.maven.eclipse.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;

import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class GameSetupState extends GameState {

	@Override
	public void onActionPlayer(TerritorySetup action) {
		//action.getPlayer().setSetupCompleted(true);
	}
	
	private void setUpGame() {
		assignColor(game.getPlayers());
		assignTerritories(game.getDeckTerritory());
		assignObjective(game.getDeckObjective());
	}

	private void assignObjective(IDeck deckObjective) {
		deckObjective.shuffle();
		for (Player player : game.getPlayers()) {
			player.setObjective(deckObjective.drawCard());
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
