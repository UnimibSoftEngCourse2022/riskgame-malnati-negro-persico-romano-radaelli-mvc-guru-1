package com.mvcguru.risiko.maven.eclipse.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.controller.GameController;
import com.mvcguru.risiko.maven.eclipse.exception.GiocatoreEsistenteException;
import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.Game;

public class LobbyState extends GameState{
	private static final Logger LOGGER = LoggerFactory.getLogger(LobbyState.class);
	
	@Override
    public void onAzioneGiocatore(GameEntry gameEntry) throws PartitaPienaException {

		LOGGER.info("LobbyState: aggiunta giocatore - azione rilevata");
        try {
            LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto" + gameEntry.getPlayer().getName());
            LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto" + game.getPlayers().size());
            System.out.println("Aggiunta giocatore " + gameEntry.getPlayer().getName());
            System.out.println("Aggiunto giocatore " + game.getPlayers().size());

            
            game.aggiungiGiocatore(gameEntry.getPlayer());
            
            LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto" + game.getPlayers().size());
        } catch (GiocatoreEsistenteException e) {
            // Lancia un messaggio di errore
        }
        
    }    
     // if (game.getPlayers().size() == game.getConfiguration().getNumberOfPlayers()) {
//      //game.inizioPartita();
 // }

}