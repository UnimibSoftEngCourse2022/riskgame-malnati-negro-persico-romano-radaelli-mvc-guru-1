package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.AttackRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.DefenderNoticeBody;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.util.Arrays;
import java.util.Collections;

@Data
@SuperBuilder
public class Turn implements Serializable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Turn.class);

    private Player player;
    
    @Builder.Default
    private int numberOfTroops = 0;
    
    private int indexTurn;
	
	private Territory attackerTerritory;
	
	private Territory defenderTerritory;
	
    public void numberOfTroopsCalculation(List<Territory> territories) throws IOException {
        numberOfTroops += territories.size() / 3;
        numberOfTroops += continentCheck(territories);
		LOGGER.info("Number of troops: {}", numberOfTroops);
      //player.getGame().broadcast(player.getGame().getId(),player.getUserName(), numberOfTroops); //da cambiare
    }

    public int continentCheck(List<Territory> territories) throws IOException {
        int troops = 0;
        List<Continent> continent = player.getGame().getContinents();
        List<String> territoriesName = territories.stream().map(Territory::getName).collect(Collectors.toList());

        for (Continent c : continent) {
            List<String> continentName = c.getTerritories().stream().map(Territory::getName).collect(Collectors.toList());
            if (territoriesName.containsAll(continentName)) {
                troops += c.getBonusArmies();
            } else {
                LOGGER.info("Player doesn't have all the territories of the continent");
            }
        }
        return troops;
    }

    public void comboCardsCheck(List<TerritoryCard> comboCards) {
        if (comboCards.size() != 3) {
            return;
        }
        long distinctSymbols = comboCards.stream().map(TerritoryCard::getSymbol).distinct().count();
        if (distinctSymbols == 1) {
        	numberOfTroops = troopsForSingleSymbolCombo(comboCards);
        } else if (distinctSymbols == 3 || comboCards.stream().anyMatch(card -> card.getSymbol() == CardSymbol.JOLLY)) {
        	numberOfTroops = troopsForMixedCombo(comboCards);
        }
        return;
    }
    
    private int troopsForSingleSymbolCombo(List<TerritoryCard> comboCards) {
        switch (comboCards.get(0).getSymbol()) {
            case ARTILLERY:
                return 4;
            case CAVALRY:
                return 6;
            case INFANTRY:
                return 8;
            default:
                return 0;
        }
    }

    private int troopsForMixedCombo(List<TerritoryCard> comboCards) {
        int troops = comboCards.stream().anyMatch(card -> card.getSymbol() == CardSymbol.JOLLY) ? 12 : 10;
        for (TerritoryCard card : comboCards) {
            if (player.getTerritories().contains(card.getTerritory()))
                troops += 2;
        }
        return troops;
    }
	
//	public void attack(AttackRequestBody attackRequestBody, DefenderNoticeBody defenderNoticeBody) {
//		if(player != player.getGame().findPlayerByUsername(attackRequestBody.getAttackerTerritory().getIdOwner())) {
//			LOGGER.info("Error in the attack phase, the player is not the attacker.");
//			return;
//		}
//		else {
//			LOGGER.info("The player is the attacker.");
//		}
//		
//		attackerTerritory = attackRequestBody.getAttackerTerritory();
//		defenderTerritory = attackRequestBody.getDefenderTerritory();
//		
//		
//	    int numAttDice = attackRequestBody.getNumDice();
//	    int numDefDice = defenderNoticeBody.getNumAttDice();
//	    
//	    Integer[] attRolls = new Integer[numAttDice];
//	    Integer[] defRolls = new Integer[numDefDice];
//	    
//	    for (int i = 0; i < numAttDice; i++) {
//	       attRolls[i] = (int) (Math.random() * 6) + 1;
//	    }
//	    
//	    for (int i = 0; i < numDefDice; i++) {
//	        defRolls[i] = (int) (Math.random() * 6) + 1;
//	    }
//	    
//	    Arrays.sort(attRolls, Collections.reverseOrder());
//	    Arrays.sort(defRolls, Collections.reverseOrder());
//	    
//	    int numComparisons = Math.min(numAttDice, numDefDice);
//	    int attLosses = 0;
//	    int defLosses = 0;
//	    
//	    for (int i = 0; i < numComparisons; i++) {
//	        if (attRolls[i] > defRolls[i]) 
//	            defLosses++;
//	        else
//	            attLosses++;
//	    }
//	    LOGGER.info("Attacker losses: {} | Defender losses: {}", attLosses, defLosses);
//	    
//	    if(player.getTerritoryByName(defenderTerritory.getName()).getArmies() > defLosses) {
//	    	 player.getTerritoryByName(attackerTerritory.getName())
//		    	.setArmies(player.getTerritoryByName(attackerTerritory.getName()).getArmies() - attLosses);
//	    	 
//	    	 player.getTerritoryByName(defenderTerritory.getName())
//		    	.setArmies(player.getTerritoryByName(defenderTerritory.getName()).getArmies() - defLosses);
//	    	 
//	    	 player.getGame().broadcast();
//	    }
//		else {
//			player.getTerritoryByName(defenderTerritory.getName())
//			.setIdOwner(attackerTerritory.getIdOwner());
//			
//			player.getGame().broadcast(player.getGame().getId(), player.getUserName(), player); //da cambiare
//		}
//	}
	
	public void moveTroops(int numTroops) {
		player.getTerritoryByName(attackerTerritory.getName()).setArmies(
				player.getTerritoryByName(attackerTerritory.getName()).getArmies() - numTroops);
		player.getTerritoryByName(defenderTerritory.getName()).setArmies(
				player.getTerritoryByName(defenderTerritory.getName()).getArmies() + numTroops);
		player.getGame().broadcast();
	}


    



    private Integer[] rollDice(int numDice) {
        SecureRandom random = new SecureRandom();
        return random.ints(numDice, 1, 7).boxed().toArray(Integer[]::new);
    }

    private void calculateLosses(Integer[] attRolls, Integer[] defRolls) {
        int numComparisons = Math.min(attRolls.length, defRolls.length);
        for (int i = 0; i < numComparisons; i++) {
            if (attRolls[i] > defRolls[i]) {
                LOGGER.info("Defender loses a unit");
            } else {
                LOGGER.info("Attacker loses a unit");
            }
        }
    }
    
//  public void attack(AttackRequestBody attackRequestBody, DefenderNoticeBody defenderNoticeBody) {
//  int numAttDice = attackRequestBody.getNumDice();
//  int numDefDice = defenderNoticeBody.getNumAttDice();
//
//  Integer[] attRolls = rollDice(numAttDice);
//  Integer[] defRolls = rollDice(numDefDice);
//
//  Arrays.sort(attRolls, Collections.reverseOrder());
//  Arrays.sort(defRolls, Collections.reverseOrder());
//
//  calculateLosses(attRolls, defRolls);
//}
}