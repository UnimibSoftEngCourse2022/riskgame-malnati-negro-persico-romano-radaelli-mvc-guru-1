package com.mvcguru.risiko.maven.eclipse.model.player;

import com.mvcguru.risiko.maven.eclipse.model.Game;

public class Player {
    private String name;
    private int playerId;
	private Game game;

    public Player(String name, int playerId) {
        this.name = name;
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

	public void setGame(Game game) {
		this.game = game;
	}
}
