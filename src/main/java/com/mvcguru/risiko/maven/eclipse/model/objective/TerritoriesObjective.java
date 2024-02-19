package com.mvcguru.risiko.maven.eclipse.model.objective;

import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;

public class TerritoriesObjective extends Objective{


	@Override
	public boolean isComplete(IGame game) {
		int territoriesWithRequiredTroops = 0;
		if(nTerritory == 24)
			if (game.getCurrentTurn().getPlayer().getTerritories().size() >= nTerritory)
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
