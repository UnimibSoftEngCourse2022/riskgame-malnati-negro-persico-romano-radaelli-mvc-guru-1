package com.mvcguru.risiko.maven.eclipse.states;


import java.io.Serializable;

import com.mvcguru.risiko.maven.eclipse.model.Game;

public abstract class GameState implements Serializable{

	
	abstract void startGame(Game game);
	
	abstract void playTurn(Game game);
	
	abstract void endGame(Game game);

}
