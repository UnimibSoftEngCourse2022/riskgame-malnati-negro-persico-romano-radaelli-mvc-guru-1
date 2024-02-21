package com.mvcguru.risiko.maven.eclipse.actions;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ConquerAssignment extends ActionPlayer{
	
	int numTroops;

	@Override
	public void accept(GameState gameState)throws GameException, DatabaseConnectionException, UserException{
		gameState.onActionPlayer(this);
	}
}
