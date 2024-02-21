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
	public boolean isComplete(IGame game, String usernameDefender) {
	    Player attacker = game.getCurrentTurn().getPlayer();
	    Player defender = game.findPlayerByUsername(usernameDefender);

	    boolean defenderEliminated = defender.getTerritories().size() == 0 && defender.getColor() == colorArmy;
	    if (defenderEliminated) {
	        return true;
	    }

	    return attacker.getColor() == colorArmy && attacker.getTerritories().size() >= nTerritory;
	}

}
