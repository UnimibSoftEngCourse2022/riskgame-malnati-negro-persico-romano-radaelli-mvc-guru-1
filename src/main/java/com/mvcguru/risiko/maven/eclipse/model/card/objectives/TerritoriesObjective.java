package com.mvcguru.risiko.maven.eclipse.model.card.objectives;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;

public class TerritoriesObjective extends ObjectiveCard{
	
	@JsonProperty("nTerritory")
	protected int nTerritory;

	@Override
	public boolean isComplete(IGame game, String username) {
		int territoriesWithRequiredTroops = 0;
		if(nTerritory == 24)
			if (game.findPlayerByUsername(username).getTerritories().size() >= nTerritory)
				isComplete = true;
		else if (nTerritory == 18) {
			for (Territory territory : game.getCurrentTurn().getPlayer().getTerritories()) {
		        if (territory.getArmies() >= 2) {
		            territoriesWithRequiredTroops++;
		        }
		    }
		    isComplete = territoriesWithRequiredTroops >= nTerritory;
		}
		return isComplete;
	}

}
