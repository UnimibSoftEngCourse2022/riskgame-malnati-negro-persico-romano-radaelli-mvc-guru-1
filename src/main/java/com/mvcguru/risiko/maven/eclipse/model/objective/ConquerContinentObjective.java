package com.mvcguru.risiko.maven.eclipse.model.objective;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

public class ConquerContinentObjective extends Objective{

	@Override
	public boolean isComplete(IGame game, String username) {
		Player player = game.findPlayerByUsername(username);
		if (continent3 != 7) {
			if (player.getTerritories().containsAll(game.getContinents().get(continent1).getTerritories())
				&& player.getTerritories().containsAll(game.getContinents().get(continent2).getTerritories())) 
				isComplete = true;
		}
		else {
			if (player.getTerritories().containsAll(game.getContinents().get(continent1).getTerritories())
				&& player.getTerritories().containsAll(game.getContinents().get(continent2).getTerritories()))
				for (Continent continent : game.getContinents())
					if (continent != game.getContinents().get(continent1) && continent != game.getContinents().get(continent2) && 
							player.getTerritories().containsAll(continent.getTerritories()))
						isComplete = true;
		}
		return isComplete;
	}

}
