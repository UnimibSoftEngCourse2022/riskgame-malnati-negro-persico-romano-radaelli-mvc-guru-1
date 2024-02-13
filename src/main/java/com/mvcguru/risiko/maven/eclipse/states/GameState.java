package com.mvcguru.risiko.maven.eclipse.states;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = LobbyState.class, name = "LobbyState")
    // Aggiungi qui altre annotazioni @JsonSubTypes per i futuri stati
})
public abstract class GameState implements Serializable {

    @JsonIgnore
    protected IGame game;

    protected GameState() {
    }


    public abstract void onActionPlayer(GameEntry gameEntry) throws FullGameException;

    public void playTurn() {
        // Implementazione di default
    }
}
