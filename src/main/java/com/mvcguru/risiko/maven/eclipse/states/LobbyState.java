package com.mvcguru.risiko.maven.eclipse.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.GameExit;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class LobbyState extends GameState{
	private static final Logger LOGGER = LoggerFactory.getLogger(LobbyState.class);
	
	@Override
    public void onActionPlayer(GameEntry gameEntry) throws FullGameException, GameException, DatabaseConnectionException, UserException{ 
        game.addPlayer(gameEntry.getPlayer());        
        if (game.getPlayers().size() == game.getConfiguration().getNumberOfPlayers()) {
        	game.setState(GameSetupState.builder().game(game).build());        	
        	game.getState().setUpGame();
        	GameRepository.getInstance().updateState(game.getId(), game.getState());
        }
    }
	
	@Override
	public void onActionPlayer (GameExit gameExit) {
		LOGGER.info("LobbyState: uscita giocatore - azione rilevata");
		game.getPlayers().remove(gameExit.getPlayer());
	}
}