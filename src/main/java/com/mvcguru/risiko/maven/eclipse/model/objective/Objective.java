package com.mvcguru.risiko.maven.eclipse.model.objective;

import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

import lombok.Data;

@Data
public abstract class Objective {
	
	protected String description;
	
	protected int nTerritory;
	
	protected Continent continent1;
	
	protected Continent continent2;
	
	protected Continent continent3;
	
	protected PlayerColor colorArmy ;

	protected boolean isComplete;
	
	public abstract boolean isComplete(IGame game);
	
	

}