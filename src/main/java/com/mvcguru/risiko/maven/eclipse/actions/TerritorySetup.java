package com.mvcguru.risiko.maven.eclipse.actions;

import java.io.IOException;

import com.mvcguru.risiko.maven.eclipse.controller.body_request.SetUpBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TerritorySetup extends ActionPlayer{
	
	protected transient SetUpBody setUpBody;
	@Override
	public void accept(GameState gameState) throws FullGameException, GameException, DatabaseConnectionException, UserException, IOException {
		gameState.onActionPlayer(this);
	}
}
