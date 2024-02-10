package com.mvcguru.risiko.maven.eclipse.states;


import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.Game;

public abstract class GameState {
	
	protected Game game;

	
	public void onAzioneGiocatore(GameEntry gameEntry) throws PartitaPienaException {   }

}
