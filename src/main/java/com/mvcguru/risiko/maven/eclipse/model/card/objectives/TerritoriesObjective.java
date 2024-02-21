package com.mvcguru.risiko.maven.eclipse.model.card.objectives;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TerritoriesObjective extends ObjectiveCard{
	
	@JsonProperty("objective")
	private String description;
	
	@JsonProperty("nTerritory")
	protected int nTerritory;

	@Override
	public boolean isComplete(IGame game, String username) {
		int territoriesWithRequiredTroops = 0;
		if(nTerritory == 24)
			if (game.findPlayerByUsername(username).getTerritories().size() >= nTerritory)
				return true;
		else if (nTerritory == 18) {
			for (Territory territory : game.getCurrentTurn().getPlayer().getTerritories()) {
		        if (territory.getArmies() >= 2) {
		            territoriesWithRequiredTroops++;
		        }
		    }
		    return territoriesWithRequiredTroops >= nTerritory;
		}
		return false;
	}

}
