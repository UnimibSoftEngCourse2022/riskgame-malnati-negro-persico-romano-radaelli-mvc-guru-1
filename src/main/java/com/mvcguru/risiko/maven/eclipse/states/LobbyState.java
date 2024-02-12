package com.mvcguru.risiko.maven.eclipse.states;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.AlreadyExistingPlayerException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class LobbyState extends GameState{
	private static final Logger LOGGER = LoggerFactory.getLogger(LobbyState.class);
	
	@Override
    public void onAzioneGiocatore(GameEntry gameEntry) throws FullGameException {

		LOGGER.info("LobbyState: aggiunta giocatore - azione rilevata");
        try {
            LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto" + gameEntry.getPlayer().getName());
            LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto" + game.getPlayers().size()); 
            game.addPlayer(gameEntry.getPlayer());
            
            LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto" + game.getPlayers().size());
        } catch (AlreadyExistingPlayerException e) {
            // Lancia un messaggio di errore
        }
        
    }    
     // if (game.getPlayers().size() == game.getConfiguration().getNumberOfPlayers()) {
//      //game.inizioPartita();
 // }

}