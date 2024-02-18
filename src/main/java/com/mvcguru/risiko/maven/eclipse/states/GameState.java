package com.mvcguru.risiko.maven.eclipse.states;

import java.io.IOException;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.GameExit;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = LobbyState.class, name = "LobbyState"),
    @JsonSubTypes.Type(value = GameSetupState.class, name = "SetupState")
})
public abstract class GameState implements Serializable {

    @JsonIgnore
    protected transient IGame game;

    protected GameState() {  }
    
    public void playTurn() {  }
    
    public void onActionPlayer(GameEntry gameEntry) throws FullGameException, GameException, DatabaseConnectionException, UserException{ }
    
    public void onActionPlayer(GameExit gameExit) { }

	public void onActionPlayer(TerritorySetup territorySetup) throws GameException, DatabaseConnectionException, UserException, FullGameException, IOException { }
	
	public void onActionPlayer(ComboRequest comboRequest) throws IOException { }
	
	public void onActionPlayer(AttackRequest attackRequest) {}

	public void setUpGame() throws GameException, DatabaseConnectionException, UserException {}
}
