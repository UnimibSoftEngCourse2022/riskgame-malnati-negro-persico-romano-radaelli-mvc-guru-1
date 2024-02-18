package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.AttackRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.DefenderNoticeBody;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.util.Arrays;
import java.util.Collections;

@Data
@SuperBuilder
public class Turn implements Serializable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Turn.class);

	private Player player;
	
	private int numberOfTroops;
	
	private Territory attackerTerritory;
	
	private Territory defenderTerritory;
	
	public void numberOfTroopsCalculation(List<Territory> territories, List<TerritoryCard> comboCards) throws IOException {
		
		numberOfTroops = territories.size() / 3;
		
		numberOfTroops += comboCardsCheck(comboCards);
		
		numberOfTroops += continentCheck(territories);
		
		LOGGER.info("Number of troops: {}", numberOfTroops);
		
		player.getGame().broadcast(player.getGame().getId(),player.getUserName(), numberOfTroops); //da cambiare
	}
	
	public int continentCheck(List<Territory> territories) throws IOException {
		int troops = 0;
/*
		List<String> territoriesName = territories.stream().map(Territory::getName).toList();
		
		List<String> continentName = null;
		
		for (Continent continent : player.getGame().getContinents()) {
			continentName = continent.getTerritories().stream().map(Territory::getName).toList();

			if (territoriesName.containsAll(continentName)) {
				troops += continent.getBonusArmies();
				LOGGER.info("Continent: {}", continent.getName());
			}
		}*/
		for (Continent continent : player.getGame().getContinents()) {
			if (player.getTerritories().containsAll(continent.getTerritories())) {
				troops += continent.getBonusArmies();
				LOGGER.info("Continent: {}", continent.getName());
			}
		}
		return troops;
	}
	
	
	public int comboCardsCheck(List<TerritoryCard> comboCards) {
		int troops = 0;
		
		System.out.println("Combo cards size: " + comboCards.size());
		System.out.println("Combo cards: " + comboCards);
		
		if(comboCards.size() != 3) {
            return 0;
        }
		
		if (comboCards.get(0).getSymbol() == comboCards.get(1).getSymbol() && comboCards.get(1).getSymbol() == comboCards.get(2).getSymbol()) {
			if (comboCards.get(0).getSymbol() == CardSymbol.ARTILLERY ) {
				System.out.println("Artillery");
				troops = 4;
			}
			else if (comboCards.get(0).getSymbol() == CardSymbol.CAVALRY) 
				troops = 6;
			else 
				troops = 8;
		}
		else if (comboCards.get(0).getSymbol() != comboCards.get(1).getSymbol()
				&& comboCards.get(1).getSymbol() != comboCards.get(2).getSymbol()
				&& comboCards.get(0).getSymbol() != comboCards.get(2).getSymbol()) {
			troops = 10;
		} 
		else if ((comboCards.get(0).getSymbol() == CardSymbol.JOLLY && comboCards.get(1).getSymbol() == comboCards.get(2).getSymbol())
				|| (comboCards.get(1).getSymbol() == CardSymbol.JOLLY && comboCards.get(0).getSymbol() == comboCards.get(2).getSymbol())
				|| (comboCards.get(2).getSymbol() == CardSymbol.JOLLY && comboCards.get(1).getSymbol() == comboCards.get(0).getSymbol())) {
			
			troops = 12;
		}
		
		for (TerritoryCard card : comboCards) {
			if (player.getTerritories().contains(card.getTerritory()))
				troops += 2;
		}
		System.out.println("Troops: " + troops);
		return troops;
	}

	
	public void attack(AttackRequestBody attackRequestBody, DefenderNoticeBody defenderNoticeBody) {
		if(player != player.getGame().findPlayerByUsername(attackRequestBody.getAttackerTerritory().getIdOwner())) {
			LOGGER.info("Error in the attack phase, the player is not the attacker.");
			return;
		}
		else {
			LOGGER.info("The player is the attacker.");
		}
		
		attackerTerritory = attackRequestBody.getAttackerTerritory();
		defenderTerritory = attackRequestBody.getDefenderTerritory();
		
		
	    int numAttDice = attackRequestBody.getNumDice();
	    int numDefDice = defenderNoticeBody.getNumAttDice();
	    
	    Integer[] attRolls = new Integer[numAttDice];
	    Integer[] defRolls = new Integer[numDefDice];
	    
	    for (int i = 0; i < numAttDice; i++) {
	       attRolls[i] = (int) (Math.random() * 6) + 1;
	    }
	    
	    for (int i = 0; i < numDefDice; i++) {
	        defRolls[i] = (int) (Math.random() * 6) + 1;
	    }
	    
	    Arrays.sort(attRolls, Collections.reverseOrder());
	    Arrays.sort(defRolls, Collections.reverseOrder());
	    
	    int numComparisons = Math.min(numAttDice, numDefDice);
	    int attLosses = 0;
	    int defLosses = 0;
	    
	    for (int i = 0; i < numComparisons; i++) {
	        if (attRolls[i] > defRolls[i]) 
	            defLosses++;
	        else
	            attLosses++;
	    }
	    LOGGER.info("Attacker losses: {} | Defender losses: {}", attLosses, defLosses);
	    
	    if(player.getTerritoryByName(defenderTerritory.getName()).getArmies() > defLosses) {
	    	 player.getTerritoryByName(attackerTerritory.getName())
		    	.setArmies(player.getTerritoryByName(attackerTerritory.getName()).getArmies() - attLosses);
	    	 
	    	 player.getTerritoryByName(defenderTerritory.getName())
		    	.setArmies(player.getTerritoryByName(defenderTerritory.getName()).getArmies() - defLosses);
	    	 
	    	 player.getGame().broadcast();
	    }
		else {
			player.getTerritoryByName(defenderTerritory.getName())
			.setIdOwner(attackerTerritory.getIdOwner());
			
			player.getGame().broadcast(player.getGame().getId(), player.getUserName(), player); //da cambiare
		}
	}
	
	public void moveTroops(int numTroops) {
		player.getTerritoryByName(attackerTerritory.getName()).setArmies(
				player.getTerritoryByName(attackerTerritory.getName()).getArmies() - numTroops);

		player.getTerritoryByName(defenderTerritory.getName()).setArmies(
				player.getTerritoryByName(defenderTerritory.getName()).getArmies() + numTroops);

		player.getGame().broadcast();
	}
}