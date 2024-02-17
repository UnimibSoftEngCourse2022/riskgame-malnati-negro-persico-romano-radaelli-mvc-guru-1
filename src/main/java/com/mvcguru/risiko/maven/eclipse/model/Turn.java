package com.mvcguru.risiko.maven.eclipse.model;

import java.io.Serializable;
import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Turn implements Serializable{

	private Player player;
	
	private int numberOfTroops;
	
	private List<TerritoryCard> comboCards;
	
	public void numberOfTroopsCalculation(List<Territory> territories) {
		
		numberOfTroops = territories.size() / 3;
		
		numberOfTroops += comboCardsCheck();
		
		//numberOfTroops += continentsCheck(territories);
		
	}
	
	public int comboCardsCheck() {
		int troops = 0;
		
		if(comboCards.size() != 3) {
            return 0;
        }
		
		if (comboCards.get(0).getSymbol() == comboCards.get(1).getSymbol() && comboCards.get(1).getSymbol() == comboCards.get(2).getSymbol()) {
			if (comboCards.get(0).getSymbol() == CardSymbol.ARTILLERY ) 
				troops = 4;
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
			
			troops = 10;
		}
		
		for (TerritoryCard card : comboCards) {
			if (player.getTerritories().contains(card.getTerritory()))
				troops += 2;
		}
		
		return troops;
	}
	
}
