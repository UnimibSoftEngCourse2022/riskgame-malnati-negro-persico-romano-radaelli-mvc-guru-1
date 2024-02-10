package com.mvcguru.risiko.maven.eclipse.model;

import java.util.LinkedList;

import com.mvcguru.risiko.maven.eclipse.exception.GiocatoreEsistenteException;
import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game extends IGame {
    
    private transient LinkedList<GameState> stackStati = new LinkedList<>();
	private GameState stateGame;
	private Game game;
    
	public Game(int id, GameConfiguration configuration) {
		super();
		this.id = id;
		this.configuration = configuration;
	}

    public synchronized void aggiungiGiocatore(Player g) throws PartitaPienaException, GiocatoreEsistenteException {
        if (players.size() == configuration.getNumberOfPlayers()) {
            throw new PartitaPienaException("Partita piena");
        }
        if (players.contains(g)) {
            throw new GiocatoreEsistenteException();
        }
        System.out.println("Aggiunta giocatore " + g.getName());
        players.add(g);
        System.out.println("Aggiunto giocatore " + players.size());
        game.setGame(this);
    }
}