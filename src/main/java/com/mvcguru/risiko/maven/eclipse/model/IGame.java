package com.mvcguru.risiko.maven.eclipse.model;

import java.lang.module.Configuration;
import java.util.ArrayList;

import com.mvcguru.risiko.maven.eclipse.exception.GiocatoreEsistenteException;
import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class IGame {
	
	protected int id;
	
	protected GameConfiguration configuration;
	
	@Builder.Default
    protected ArrayList<Player> players = new ArrayList<>();

    protected GameState state;
    
    public abstract int getId();
    
    public abstract void aggiungiGiocatore(Player g) throws PartitaPienaException, GiocatoreEsistenteException;
    
    public abstract void setStato(GameState state);


}
