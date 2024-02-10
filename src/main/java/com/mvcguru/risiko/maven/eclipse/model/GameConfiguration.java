package com.mvcguru.risiko.maven.eclipse.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class GameConfiguration implements Serializable {

    @JsonProperty("difficolta")
    private GameMode mode;

    @JsonProperty("players")
    private int numberOfPlayers;

    @JsonProperty("nomeMappa")
    private String idMap;

	private enum GameMode {
        Easy,
        Medium,
        Hard
    }
}