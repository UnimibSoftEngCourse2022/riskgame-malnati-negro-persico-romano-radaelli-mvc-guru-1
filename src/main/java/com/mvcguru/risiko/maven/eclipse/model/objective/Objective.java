package com.mvcguru.risiko.maven.eclipse.model.objective;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.Continent;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class Objective {
	
	@JsonProperty("objective")
	protected String description;
	
	@JsonProperty("nTerritory")
	protected int nTerritory;
	
	@JsonProperty("continent1")
	protected int continent1;
	
	@JsonProperty("continent2")
	protected int continent2;
	
	@JsonProperty("continent3")
	protected int continent3;
	
	@JsonProperty("colorArmy")
	protected PlayerColor colorArmy ;

	protected boolean isComplete = false;
	
	public boolean isComplete(IGame game) { return false; }

	public boolean isComplete(IGame game, String usernameDefender) { return false; }
	
}