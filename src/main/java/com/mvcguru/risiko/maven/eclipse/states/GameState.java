package com.mvcguru.risiko.maven.eclipse.states;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = LobbyState.class, name = "LobbyState")
})
public abstract class GameState implements Serializable {

    @JsonIgnore
    protected transient IGame game;

    protected GameState() {  }
    
    public void playTurn() {  }
    
    public void onActionPlayer(GameEntry gameEntry) throws FullGameException{ }

	public void onActionPlayer(TerritorySetup territorySetup) { }

	public void setUpGame() {}
}
