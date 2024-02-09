package com.mvcguru.risiko.maven.eclipse.model;

public class GameConfinguration {
	
	private enum gameMode {
	    EASY,
	    MEDIUM,
	    Hard
	}
	
	private gameMode mode;
	
	private int numberOfPlayers;
	
	private GameMap map;

	public gameMode getMode() {
		return mode;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public GameMap getMap() {
		return map;
	}
	
	

}
