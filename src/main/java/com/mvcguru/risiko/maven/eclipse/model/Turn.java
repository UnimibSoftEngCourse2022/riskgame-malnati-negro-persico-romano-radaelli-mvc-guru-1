package com.mvcguru.risiko.maven.eclipse.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.controller.body_request.ResultNoticeBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Turn implements Serializable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Turn.class);

    private Player player;
    
    @Builder.Default
    private int numberOfTroops = 0;
    
    private int indexTurn;
	
	private Territory attackerTerritory;
	
	private Territory defenderTerritory;
	
	private int numAttDice;
	
	private int numDefDice;
	
	private boolean isConquered = false;
	
    public void numberOfTroopsCalculation(List<Territory> territories) {
        numberOfTroops += territories.size() / 3;
        numberOfTroops += continentCheck(territories);
		LOGGER.info("Number of troops: {}", numberOfTroops);
    }

    public int continentCheck(List<Territory> territories){
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
        if (comboCards.size() == 3) {
        	int distinctSymbols = (int) comboCards.stream().map(TerritoryCard::getSymbol).distinct().count();
            
            switch(distinctSymbols) {
    	        case 1:
    	        	numberOfTroops = troopsForSingleSymbolCombo(comboCards);
    	        	break;
    	        case 2 :
    	        	numberOfTroops = troopsForJollydCombo(comboCards);
    	        		break;
    	    	case 3:
    	    		numberOfTroops = troopsForTris(comboCards);
    	    		break;
    	    	default:
    	    		return;
            }
            
            for (TerritoryCard card : comboCards) {
                if (player.getTerritories().contains(card.getTerritory()))
                	numberOfTroops += 2;
            }
        }
        
    }
    
    private int troopsForSingleSymbolCombo(List<TerritoryCard> comboCards) {
        switch (comboCards.get(0).getSymbol()) {
            case ARTILLERY:
                return 4;
            case CAVALRY:
                return 8;
            case INFANTRY:
                return 6;
            default:
                return 0;
        }
    }

    private int troopsForJollydCombo(List<TerritoryCard> comboCards) {
    	long jollyCount = comboCards.stream().filter(card -> card.getSymbol() == CardSymbol.JOLLY).count();
        if (jollyCount == 1) 
            return 12;
        return 0;
    }
    
	private int troopsForTris(List<TerritoryCard> comboCards) {
		if(!comboCards.stream().anyMatch(card -> card.getSymbol() == CardSymbol.JOLLY))
			return 10;
		return 0;
	}
	
	public void moveTroops(int numTroops) {
		player.getTerritoryByName(attackerTerritory.getName()).setArmies(
				player.getTerritoryByName(attackerTerritory.getName()).getArmies() - numTroops);
		player.getTerritoryByName(defenderTerritory.getName()).setArmies(
				player.getTerritoryByName(defenderTerritory.getName()).getArmies() + numTroops);
	}
	
	//////////////////////////////////////
	
	public void attack() throws GameException, DatabaseConnectionException, UserException {
		    
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
	    
	    if(defenderTerritory.getArmies() > defLosses) {
	    	attackerTerritory.setArmies(attackerTerritory.getArmies() - attLosses); 
	    	GameRepository.getInstance().updateTerritoryArmies(attackerTerritory.getName(), player.getGameId(), attackerTerritory.getArmies());
	    	defenderTerritory.setArmies(defenderTerritory.getArmies() - defLosses);
	    	GameRepository.getInstance().updateTerritoryArmies(defenderTerritory.getName(), player.getGameId(), defenderTerritory.getArmies());
			ResultNoticeBody result = ResultNoticeBody.builder().isConquered(false).lostAttTroops(attLosses).lostDefTroops(defLosses).build();
	    	player.getGame().broadcast(player.getGame().getId(), player.getUserName(), result);
			resetBattleInfo();
	    }
		else {
			isConquered = true;
			defenderTerritory.setIdOwner(attackerTerritory.getIdOwner());
			ResultNoticeBody result = ResultNoticeBody.builder().isConquered(true).lostAttTroops(attLosses).lostDefTroops(defLosses).build();
			player.getGame().broadcast(player.getGame().getId(), player.getUserName(), result);
		}
	}
	
	public void resetBattleInfo() {
		attackerTerritory = null;
        defenderTerritory = null;
        numAttDice = 0;
        numDefDice = 0;
	}
	
}