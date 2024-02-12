package com.mvcguru.risiko.maven.eclipse.actions;

import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class GameEntry extends ActionPlayer {

	@Override
    public void accept(GameState gameState) throws PartitaPienaException {
		gameState.onAzioneGiocatore(this);
    }

	
}