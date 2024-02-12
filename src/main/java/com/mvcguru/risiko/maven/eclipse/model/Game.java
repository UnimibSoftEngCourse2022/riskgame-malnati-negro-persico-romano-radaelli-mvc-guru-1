package com.mvcguru.risiko.maven.eclipse.model;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.ActionPlayer;
import com.mvcguru.risiko.maven.eclipse.controller.GameController;
import com.mvcguru.risiko.maven.eclipse.controller.MessageBrokerSingleton;
import com.mvcguru.risiko.maven.eclipse.exception.AlreadyExistingPlayerException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
        //To DO scelta colore armate
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