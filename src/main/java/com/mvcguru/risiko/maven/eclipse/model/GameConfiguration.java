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
	
	private GameMode mode;
	
	private int numberOfPlayers;
	
	private String idMap;
	
}
