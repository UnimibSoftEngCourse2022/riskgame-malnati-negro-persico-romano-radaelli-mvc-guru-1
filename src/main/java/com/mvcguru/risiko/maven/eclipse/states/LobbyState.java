package com.mvcguru.risiko.maven.eclipse.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class LobbyState extends GameState{
	private static final Logger LOGGER = LoggerFactory.getLogger(LobbyState.class);
	
	@Override
    public void onActionPlayer(GameEntry gameEntry) throws FullGameException{

		LOGGER.info("LobbyState: aggiunta giocatore - azione rilevata");
        LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto {}", gameEntry.getPlayer().getUserName());
        LOGGER.info("LobbyState: numero giocatori presenti {}", game.getPlayers().size()); 
            
        game.addPlayer(gameEntry.getPlayer());
            
        LOGGER.info("LobbyState: aggiunta giocatore - giocatore aggiunto {}", game.getPlayers().size());
        
        if (game.getPlayers().size() == game.getConfiguration().getNumberOfPlayers()) {
        	LOGGER.info("LobbyState: inizio partita");

        	game.setState(GameSetupState.builder().game(game).build());
        	game.getState().setupGame();
        }
        
    }    
    

}