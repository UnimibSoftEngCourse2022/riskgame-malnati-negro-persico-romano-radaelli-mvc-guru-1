package com.mvcguru.risiko.maven.eclipse.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter

public class GameConfiguration {
	
	private enum GameMode {
	    EASY,
	    MEDIUM,
	    Hard
	}
	
	private GameMode mode;
	
	private int numberOfPlayers;
	
	private String idMap;
	
}
