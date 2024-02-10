package com.mvcguru.risiko.maven.eclipse.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class GameConfiguration {
	
	private enum GameMode {
	    Easy,
	    Medium,
	    Hard
	}
	
	public String getModeString() {
		return mode.name();
    }
	
	private GameMode mode;
	
	private int numberOfPlayers;
	
	private String idMap;
	
}
