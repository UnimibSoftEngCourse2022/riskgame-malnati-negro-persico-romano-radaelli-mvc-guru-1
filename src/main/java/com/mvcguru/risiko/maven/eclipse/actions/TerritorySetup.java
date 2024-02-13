package com.mvcguru.risiko.maven.eclipse.actions;

import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

public class TerritorySetup extends ActionPlayer{

	@Override
	public void accept(GameState gameState) throws FullGameException {
		gameState.onActionPlayer(this);
	}
}
