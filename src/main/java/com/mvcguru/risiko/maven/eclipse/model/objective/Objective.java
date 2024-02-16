package com.mvcguru.risiko.maven.eclipse.model.objective;

import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

import lombok.Data;

@Data
public abstract class Objective {
	
	protected String description;
	
	protected int n_territory;
	
	protected Continent continent1, continent2, continent3;
	
	protected PlayerColor col_army ;
	
	protected boolean isComplete;
	
	public abstract boolean isComplete(IGame game);
	
	

}