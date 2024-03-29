package com.mvcguru.risiko.maven.eclipse.actions;

import java.io.IOException;
import java.io.Serializable;

import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class ActionPlayer implements Serializable {
	
	protected transient Player player;

    protected ActionPlayer() {}

    public abstract void accept(GameState gameState) throws FullGameException, GameException, DatabaseConnectionException, UserException, IOException;
    
}