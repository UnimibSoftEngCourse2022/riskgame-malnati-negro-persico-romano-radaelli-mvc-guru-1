package com.mvcguru.risiko.maven.eclipse.states;

import java.io.IOException;

import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;

public class StartTurnState extends GameState{
	
	@Override
	public void onActionPlayer(ComboRequest comboRequest) throws IOException {
		game.getCurrentTurn().setComboCards(comboRequest.getComboRequestBody().getComboCards());
		game.getCurrentTurn().numberOfTroopsCalculation(
				game.findPlayerByUsername(
						comboRequest.getComboRequestBody().getUsername()).getTerritories());
	}
	
	@Override
	public void onActionPlayer(TerritorySetup action) {
		game.broadcast();
	}
}
