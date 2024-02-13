package com.mvcguru.risiko.maven.eclipse.model;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.ActionPlayer;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
public abstract class IGame {
	protected static final Logger LOGGER = LoggerFactory.getLogger(IGame.class);

	
	protected String id;
	
	protected GameConfiguration configuration;
	
	@Builder.Default
    protected ArrayList<Player> players = new ArrayList<>();

    protected GameState state;

    public abstract void addPlayer(Player g) throws FullGameException;
    
    public abstract void onActionPlayer(ActionPlayer action) throws FullGameException;
    
    public abstract void broadcast();
   
}
