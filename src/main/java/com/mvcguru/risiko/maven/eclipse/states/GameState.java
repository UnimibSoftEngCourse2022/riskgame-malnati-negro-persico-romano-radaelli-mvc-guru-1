package com.mvcguru.risiko.maven.eclipse.states;


import com.mvcguru.risiko.maven.eclipse.model.Game;

public abstract class GameState {

	
	abstract void startGame(Game game);
	
	abstract void playTurn(Game game);
	
	abstract void endGame(Game game);

}
