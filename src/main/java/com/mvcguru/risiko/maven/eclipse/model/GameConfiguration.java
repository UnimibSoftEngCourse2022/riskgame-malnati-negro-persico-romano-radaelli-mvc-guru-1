package com.mvcguru.risiko.maven.eclipse.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.util.Objects;

@Data
@SuperBuilder
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
	
	public void setModeFromString(String modeString) {
        try {
            this.mode = GameMode.valueOf(modeString);
        } catch (IllegalArgumentException e) {
            // Gestione dell'eccezione nel caso in cui la stringa non corrisponda a nessuna costante dell'enumerazione
            System.out.println("Modalità di gioco non valida: " + modeString);
        }
    }
	
	public String getModeString() {
		return mode.name();
    }
}