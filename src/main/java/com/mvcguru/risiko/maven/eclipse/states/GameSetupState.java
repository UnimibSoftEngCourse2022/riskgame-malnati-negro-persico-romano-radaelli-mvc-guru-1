package com.mvcguru.risiko.maven.eclipse.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.deck.DeckObjectives;
import com.mvcguru.risiko.maven.eclipse.model.deck.DeckTerritories;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class GameSetupState extends GameState {

	@Override
	public void onActionPlayer(TerritorySetup action) {
		
		
	}
	
	private void setUpGame() {
		assignColor(game.getPlayers());
		assignTerritories(game.getDeckTerritory());
		assignObjective(game.getDeckObjective());
	}

	private void assignObjective(IDeck deckObjective) {
		for (Player player : game.getPlayers()) {
			player.setObjective(deckObjective.drawObjectiveCard());
		}
		
	}
    
	//TO DO da fare con il draw
	/*
	private void assignTerritories(IDeck deckTerritory) {
		int i = 0;
		for (ICard card : deckTerritory.getTerritoryCards() {
			game.getPlayers().get(i % game.getPlayers().size()).getTerritories().add(card.getTerritory());
			i++;
		}
	}*/

	private void assignColor(List<Player> players) {
		List<PlayerColor> colors = new ArrayList<>(Arrays.asList(PlayerColor.values()));
		Collections.shuffle(colors);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setColor(colors.get(i));
		}
		
	}


}
