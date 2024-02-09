package com.mvcguru.risiko.maven.eclipse.states;

import com.mvcguru.risiko.maven.eclipse.model.Game;

public interface GameState {
	
	void startGame(Game game);
	
	void playTurn(Game game);
	
	void endGame(Game game);

}
