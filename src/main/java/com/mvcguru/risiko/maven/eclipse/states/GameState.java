package com.mvcguru.risiko.maven.eclipse.states;

import java.io.Serializable;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.Game;
import com.mvcguru.risiko.maven.eclipse.model.IGame;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class GameState implements Serializable {
	
	protected IGame game;
	
	protected GameState() {
		
	}
	
	public void onAzioneGiocatore(GameEntry gameEntry) throws FullGameException {   }


	public void playTurn(Game game) {	
		
	}

}
