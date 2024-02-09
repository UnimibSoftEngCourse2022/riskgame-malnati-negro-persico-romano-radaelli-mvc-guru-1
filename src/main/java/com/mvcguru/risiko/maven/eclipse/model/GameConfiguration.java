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
	
	@Getter private GameMap map;
	
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
}
