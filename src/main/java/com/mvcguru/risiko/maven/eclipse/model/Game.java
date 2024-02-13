package com.mvcguru.risiko.maven.eclipse.model;

import com.mvcguru.risiko.maven.eclipse.actions.ActionPlayer;
import com.mvcguru.risiko.maven.eclipse.controller.MessageBrokerSingleton;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Game extends IGame {
    
    //private transient LinkedList<GameState> stackStati = new LinkedList<>();
	
    
	public Game(String id, GameConfiguration configuration) {
		super();
		this.id = id; 
		this.configuration = configuration;
	}

    public synchronized void addPlayer(Player g) throws FullGameException {
        if (players.size() == configuration.getNumberOfPlayers()) {
            throw new FullGameException("Partita piena");
        }     
        players.add(g);
        LOGGER.info("Aggiunta giocatore - giocatore aggiunto {}", g.getUserName());
        g.setGame(this);
        //TODO scelta colore armate
    }

	@Override
	public void onActionPlayer(ActionPlayer action) throws FullGameException {
		action.accept(state);
		broadcast();
	}

	@Override
    public void broadcast() {
        MessageBrokerSingleton.getInstance().broadcast(this);
	}
}