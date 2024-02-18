package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.ActionPlayer;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
public abstract class IGame {
	protected static final Logger LOGGER = LoggerFactory.getLogger(IGame.class);

	protected String id;
	
	protected GameConfiguration configuration;
	
	protected IDeck deckObjective;
	
	protected IDeck deckTerritory;
	
	@Builder.Default
    protected ArrayList<Player> players = new ArrayList<>();

    protected GameState state;
    
    protected Turn currentTurn;
    
    protected List<Continent> continents;

    public abstract void addPlayer(Player g) throws FullGameException;
    
    public abstract void onActionPlayer(ActionPlayer action) throws FullGameException, IOException;
    
    public abstract void broadcast();
    
    public abstract <T> void broadcast(String idGame, String idUser,T object);

	public abstract Player findPlayerByUsername(String username);
	
	public abstract IDeck createTerritoryDeck(GameConfiguration configuration) throws IOException;
	
	public abstract IDeck createObjectiveDeck(GameConfiguration configuration) throws IOException;

	public void startGame() { }
	
	public abstract List<Continent> parsingContinent() throws IOException;
   
}
