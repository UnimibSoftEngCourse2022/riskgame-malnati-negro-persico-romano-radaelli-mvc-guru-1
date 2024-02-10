package com.mvcguru.risiko.maven.eclipse.states;

import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import java.io.Serializable;

public abstract class State implements Serializable {
    protected Lobby lobby;

    protected State(Lobby lobby) {
        this.lobby = lobby;
    }

    public void addPlayer(Player player) throws PartitaPienaException {
        if (lobby.getPlayers().size() < lobby.getMaxPlayers()) {
            lobby.getPlayers().add(player);
            System.out.println("Giocatore " + player.getName() + " aggiunto alla lobby.");
        } else {
            throw new PartitaPienaException("Impossibile aggiungere il giocatore. Lobby piena.");
        }
    }

    public void removePlayer(Player player) {
        lobby.getPlayers().remove(player);
        System.out.println("Giocatore " + player.getName() + " rimosso dalla lobby.");
    }

    public abstract void startGame() throws PartitaPienaException;
}
