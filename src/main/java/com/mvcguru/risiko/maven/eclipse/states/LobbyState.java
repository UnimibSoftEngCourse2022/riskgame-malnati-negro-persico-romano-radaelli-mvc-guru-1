package com.mvcguru.risiko.maven.eclipse.states;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.GiocatoreEsistenteException;
import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.Game;

public class LobbyState extends GameState{
	
	@Override
    public void onAzioneGiocatore(GameEntry gameEntry) throws PartitaPienaException {
		 try {
	            game.aggiungiGiocatore(gameEntry.getPlayer());
	        } catch (GiocatoreEsistenteException e) {
	            // Lancia un messaggio di errore
	        }
	}
}