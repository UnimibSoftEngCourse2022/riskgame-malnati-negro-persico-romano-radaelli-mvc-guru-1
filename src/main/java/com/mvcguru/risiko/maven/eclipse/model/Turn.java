package com.mvcguru.risiko.maven.eclipse.model;

import java.io.Serializable;
import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Turn implements Serializable{

	private Player player;
	
	private int numberOfTroops;
	
	private List<ICard> comboCards;
	
	public void numberOfTroopsCalculation(List<Territory> territories) {
		
		numberOfTroops = territories.size() / 3;
		
		numberOfTroops += comboCardsCheck();
		
	}
	
	public int comboCardsCheck() {
		if(comboCards.size() != 3) {
            return 0;
        }
		
		String combo = comboCards.get(0).toString() + comboCards.get(1).toString() + comboCards.get(2).toString();
		for (ICard card : comboCards) {
			System.out.println(card.toString());
			
		}
		
		switch(combo) {
			case "ARTILLERYARTILLERYARTILLERY":
				return 4;
			case "CAVALRYCAVALRYCAVALRY":
				return 6;
			case "INFANTRYINFANTRYINFANTRY":
				return 8;
			
		}
		
		return 0;
	}
	
}
