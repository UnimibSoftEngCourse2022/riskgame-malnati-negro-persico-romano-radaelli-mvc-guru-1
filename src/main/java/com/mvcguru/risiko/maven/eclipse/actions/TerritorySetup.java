package com.mvcguru.risiko.maven.eclipse.actions;

import com.mvcguru.risiko.maven.eclipse.controller.SetUpBody;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TerritorySetup extends ActionPlayer{
	
	protected transient SetUpBody setUpBody;
	@Override
	public void accept(GameState gameState) throws FullGameException {
		gameState.onActionPlayer(this);
	}
}
