package com.mvcguru.risiko.maven.eclipse.model;

import java.util.ArrayList;

import com.mvcguru.risiko.maven.eclipse.exception.GiocatoreEsistenteException;
import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public abstract class IGame {
	
	protected int id;
	
	protected GameConfiguration configuration;
	
    protected ArrayList<Player> players = new ArrayList<>();

    protected GameState state;

    public abstract void aggiungiGiocatore(Player g) throws PartitaPienaException, GiocatoreEsistenteException;
    

}
