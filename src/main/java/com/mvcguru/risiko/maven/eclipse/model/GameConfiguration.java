package com.mvcguru.risiko.maven.eclipse.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class GameConfiguration implements Serializable {

    @JsonProperty("difficolta")
    private GameMode mode;

    @JsonProperty("players")
    private int numberOfPlayers;

    @JsonProperty("nomeMappa")
    private String idMap;

    public void setMode(GameMode mode) {
		this.mode = mode;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idMap, mode, numberOfPlayers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameConfiguration other = (GameConfiguration) obj;
		return Objects.equals(idMap, other.idMap) && mode == other.mode && numberOfPlayers == other.numberOfPlayers;
	}

	@Override
	public String toString() {
		return "GameConfiguration [mode=" + mode + ", numberOfPlayers=" + numberOfPlayers + ", idMap=" + idMap + "]";
	}

	public void setIdMap(String idMap) {
		this.idMap = idMap;
	}

	public GameMode getMode() {
		return mode;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public String getIdMap() {
		return idMap;
	}

	private enum GameMode {
        Easy,
        Medium,
        Hard
    }
}
