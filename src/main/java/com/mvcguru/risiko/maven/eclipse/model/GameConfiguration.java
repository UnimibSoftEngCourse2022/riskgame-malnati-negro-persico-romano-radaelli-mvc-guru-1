package com.mvcguru.risiko.maven.eclipse.model;

import lombok.Data;
import lombok.Getter;


@Data
public class GameConfiguration {
	
	private enum GameMode {
	    EASY,
	    MEDIUM,
	    Hard
	}
	
	@Getter private GameMode mode;
	
	@Getter private int numberOfPlayers;
	
	@Getter private String idMap;
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public String getMap() {
		return idMap;
	}

	public GameMode getMode() {
		return mode;
	}
}
