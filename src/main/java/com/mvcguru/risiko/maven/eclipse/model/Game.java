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
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game extends IGame {
    
    private transient LinkedList<GameState> stackStati = new LinkedList<>();
    
	public Game(int id, GameConfiguration configuration) {
		super();
		this.id = id;
		this.configuration = configuration;
	}

    public synchronized void aggiungiGiocatore(Player g) throws PartitaPienaException, GiocatoreEsistenteException {
        if (giocatori.size() == configuration.getNumberOfPlayers()) {
            throw new PartitaPienaException();
        }
        if (giocatori.contains(g)) {
            throw new GiocatoreEsistenteException();
        }
        giocatori.add(g);
        g.setGame(this);
    }
    
	public void setStato(GameState stato) {
		this.stato = stato;
	}
}