package com.mvcguru.risiko.maven.eclipse.model.card.objectives;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class DestroyArmyObjective extends ObjectiveCard{
	
	@JsonProperty("objective")
	private String description;
	
	@JsonProperty("nTerritory")
	protected int nTerritory;
	
	@JsonProperty("colorArmy")
	protected PlayerColor colorArmy ;

	@Override
	public boolean isComplete(IGame game, String unsernameDefender) {
		
		Player attacker = game.getCurrentTurn().getPlayer();
		Player defender = game.findPlayerByUsername(unsernameDefender);
		Player colorOwner = game.findPlayerByColor(colorArmy);
		
		
		if (defender.getTerritories().size() == 0 && defender.getColor() == colorArmy) {
			return true;
		}
//		if (attacker.getColor() == colorArmy || colorOwner.getTerritories().size() == 0) {
		if (attacker.getColor() == colorArmy) {
			if (attacker.getTerritories().size() >= nTerritory) {
				return true;
			}
		}
		return false;
	}
}
