package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.controller.bodyRequest.AttackRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.bodyRequest.DefenderNoticeBody;
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

	private Player player;
	
	private int numberOfTroops;
	
	private List<TerritoryCard> comboCards;
	
	private List<Continent> continent;
	
	public void numberOfTroopsCalculation(List<Territory> territories) throws IOException {
		
		numberOfTroops = territories.size() / 3;
		System.out.println("Number of troops: " + numberOfTroops);
		
		numberOfTroops += comboCardsCheck();
		System.out.println("Number of troops: " + numberOfTroops);
		
		numberOfTroops += continentCheck(territories);
		System.out.println("Number of troops: " + numberOfTroops);
		
	}
	
	public int continentCheck(List<Territory> territories) throws IOException {
		int troops = 0;
		System.out.println("continentCheck(): " + territories);

		continent = player.getGame().parsingContinent();
		//System.out.println("Continent: " + continent.size() + continent.get(0).getTerritories());

		List<String> territoriesName = territories.stream().map(Territory::getName).toList();
		System.out.println("Territories name: " + territoriesName.toString());
		
		List<String> continentName = null;
		
		for (Continent continent : continent) {
			continentName = continent.getTerritories().stream().map(Territory::getName).toList();
			System.out.println("Territories name: " + continentName.toString());

			if (territoriesName.containsAll(continentName)) {
				troops += continent.getBonusArmies();
				System.out.println("Territories name: asasasasasas");
			}
			else
				System.out.println("Territories name: dsfdfdfdfdf");
		}
		System.out.println("Troops: " + troops);
		return troops;
	}
	
	
	
	public int comboCardsCheck() {
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
	        if (attRolls[i] > defRolls[i]) {
	            defLosses++;
	        } else {
	            attLosses++;
	        }
	    }
	    
	    System.out.println("Attacker losses: " + attLosses + " | Defender losses: " + defLosses);
	}

	}

