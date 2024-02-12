package com.mvcguru.risiko.maven.eclipse.actions;

import java.io.Serializable;

import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class ActionPlayer implements Serializable {
	protected Player player;

    protected ActionPlayer() {}

    public abstract void accept(GameState gameState) throws FullGameException;
    
}