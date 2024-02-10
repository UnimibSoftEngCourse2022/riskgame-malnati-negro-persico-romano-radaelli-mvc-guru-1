package com.mvcguru.risiko.maven.eclipse.model;

import com.mvcguru.risiko.maven.eclipse.exception.GiocatoreEsistenteException;
import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@EqualsAndHashCode(callSuper = true)
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

    public int getId() {
        return id;
    }

    public synchronized void aggiungiGiocatore(Player g) throws PartitaPienaException, GiocatoreEsistenteException {
        if (giocatori.size() == configuration.getNumberOfPlayers()) {
            throw new PartitaPienaException("Partita piena. Impossibile aggiungere giocatori.");
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
