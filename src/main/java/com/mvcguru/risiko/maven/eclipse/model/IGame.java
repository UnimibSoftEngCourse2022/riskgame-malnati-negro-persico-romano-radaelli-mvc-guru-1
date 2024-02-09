package com.mvcguru.risiko.maven.eclipse.model;

import java.lang.module.Configuration;
import java.util.ArrayList;

import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Builder;

public abstract class IGame {
	
	protected String id;
	
	protected Configuration configuration;
	
	@Builder.Default
    protected ArrayList<Player> giocatori = new ArrayList<>();

    protected GameState stato;

}
