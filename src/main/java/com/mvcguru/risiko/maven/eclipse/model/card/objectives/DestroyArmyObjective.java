package com.mvcguru.risiko.maven.eclipse.model.card.objectives;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

public class DestroyArmyObjective extends ObjectiveCard{
	
	@JsonProperty("nTerritory")
	protected int nTerritory;
	
	@JsonProperty("colorArmy")
	protected PlayerColor colorArmy ;

	@Override
	public boolean isComplete(IGame game, String unsernameDefender) {
		
		Player attacker = game.getCurrentTurn().getPlayer();
		Player defender = game.findPlayerByUsername(unsernameDefender);
		
		
		if (defender.getTerritories().size() == 0 && defender.getColor() == colorArmy) {
			return true;
		}
		if (attacker.getColor() == colorArmy || defender.getTerritories().size() == 0) {
			if (attacker.getTerritories().size() >= nTerritory) {
				return true;
			}
		}
		
		return false;
	}
}