package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.actions.ActionPlayer;
import com.mvcguru.risiko.maven.eclipse.controller.MessageBrokerSingleton;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.PlayTurnState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Game extends IGame {
	
	public Game(String id, GameConfiguration configuration) {
		super();
		this.id = id; 
		this.configuration = configuration;
	}

    public synchronized void addPlayer(Player g) throws FullGameException {
        if (players.size() == configuration.getNumberOfPlayers()) {
            throw new FullGameException("Partita piena");
        }     
        players.add(g);
        LOGGER.info("Aggiunta giocatore - giocatore aggiunto {}", g.getUserName());
        g.setGame(this);
    }

	@Override
	public void onActionPlayer(ActionPlayer action) throws FullGameException, GameException, DatabaseConnectionException, UserException, IOException {
		action.accept(state);
		broadcast();
	}

	@Override
    public void broadcast() {
        MessageBrokerSingleton.getInstance().broadcast(this);
	}
	
	@Override
	public <T> void broadcast(String idGame, String idUser,T object) {
		MessageBrokerSingleton.getInstance().broadcast(idGame, idUser, object);
	}
	
	public Player findPlayerByUsername(String username) {
        for (Player player : players) {
            if (player.getUserName().equals(username)) {
                return player;
            }
        }
        return null;
    }

	
	
	public void startGame() {
		currentTurn = Turn.builder()
                .player(players.get(0))
                .build();
		//currentTurn.numberOfTroopsCalculation(currentTurn.getPlayer().getTerritories());
		//broadcast();
	}
	
	public void changeTurn() {
		
		Player curr = currentTurn.getPlayer();
		LOGGER.info("Cambio turno - giocatore corrente {}", curr.getUserName());
		
		setState(PlayTurnState.builder().build());
        setCurrentTurn(Turn.builder()
                .player(players.get((players.indexOf(curr) + 1) % players.size()))
                .build());
		
        broadcast();
	}	
	
	
	
	public List<Continent> parsingContinent() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("continent.json").getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        Continent[] continents = mapper.readValue(json, Continent[].class);
        
        List<Continent> continent = new ArrayList<Continent>();
		for (Continent c : continents) {
			continent.add(c);
		}
		
		return continent;
		
	}
	
}