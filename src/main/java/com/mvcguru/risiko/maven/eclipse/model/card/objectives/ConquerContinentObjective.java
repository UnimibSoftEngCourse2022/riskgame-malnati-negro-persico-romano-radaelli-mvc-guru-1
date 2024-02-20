package com.mvcguru.risiko.maven.eclipse.model.card.objectives;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

public class ConquerContinentObjective extends ObjectiveCard{
	
	@JsonProperty("continent1")
	protected int continent1;
	
	@JsonProperty("continent2")
	protected int continent2;
	
	@JsonProperty("continent3")
	protected int continent3;

	@Override
	public boolean isComplete(IGame game, String username) {
		Player player = game.findPlayerByUsername(username);
		if (continent3 != 7) {
			if (player.getTerritories().containsAll(game.getContinents().get(continent1).getTerritories())
				&& player.getTerritories().containsAll(game.getContinents().get(continent2).getTerritories())) 
				return true;
		}
		else {
			if (player.getTerritories().containsAll(game.getContinents().get(continent1).getTerritories())
				&& player.getTerritories().containsAll(game.getContinents().get(continent2).getTerritories()))
				for (Continent continent : game.getContinents())
					if (continent != game.getContinents().get(continent1) && continent != game.getContinents().get(continent2) && 
							player.getTerritories().containsAll(continent.getTerritories()))
						return true;
		}
		return false;
	}

}