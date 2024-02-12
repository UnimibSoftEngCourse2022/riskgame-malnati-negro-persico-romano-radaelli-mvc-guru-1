package com.mvcguru.risiko.maven.eclipse.model.player;

import com.mvcguru.risiko.maven.eclipse.model.IGame;

import lombok.Data;

@Data
public class Player {	
	private String name;
	private int playerId;
	private IGame game;
	
    public Player(String name, int playerId) {
        this.name = name;
        this.playerId = playerId;
    }
}
