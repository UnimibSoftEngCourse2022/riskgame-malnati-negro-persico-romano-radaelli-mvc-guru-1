package com.mvcguru.risiko.maven.eclipse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
public class GameConfiguration implements Serializable {

    @JsonProperty("difficolta")
    private GameMode mode;

    @JsonProperty("players")
    private int numberOfPlayers;

    @JsonProperty("nomeMappa")
    private String idMap;

	public enum GameMode {
        EASY,
        MEDIUM,
        HARD
	}
	
	
}