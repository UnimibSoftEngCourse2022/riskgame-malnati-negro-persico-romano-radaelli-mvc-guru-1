package com.mvcguru.risiko.maven.eclipse.model.objective;

import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

public class DestroyArmyObjective extends Objective{

	@Override
	public boolean isComplete(IGame game, String username) {
		
		Player player =game.findPlayerByUsername(username);
		if (player.getTerritories().size() == 0 && player.getColor() == colorArmy) {
			isComplete = true;
		}
		isComplete = false;
		return isComplete;
	}

}
