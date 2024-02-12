package com.mvcguru.risiko.maven.eclipse.model;

import java.util.LinkedList;

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

    public synchronized void addPlayer(Player g) throws FullGameException, AlreadyExistingPlayerException {
        if (players.size() == configuration.getNumberOfPlayers()) {
            throw new FullGameException("Partita piena");
        }
        if (players.contains(g)) {
            throw new AlreadyExistingPlayerException();
        }
        System.out.println("Aggiunta giocatore " + g.getName());
        players.add(g);
        System.out.println("Aggiunto giocatore " + players.size());
        g.setGame(this);
    }
}