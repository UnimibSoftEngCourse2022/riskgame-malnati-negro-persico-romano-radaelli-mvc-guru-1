package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import com.mvcguru.risiko.maven.eclipse.actions.ActionPlayer;
import com.mvcguru.risiko.maven.eclipse.controller.MessageBrokerSingleton;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

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
        g.setGame(this);
    }

	@Override
	public void onActionPlayer(ActionPlayer action) throws FullGameException, GameException, DatabaseConnectionException, UserException, IOException {
		action.accept(state);
		LOGGER.info("Sto per fare broadcast alla partita {}", id);
		broadcast();
	}
	
	@Override
    public void broadcast() {
        MessageBrokerSingleton.getInstance().broadcast(this);
	}
	
	@Override
	public <T> void broadcast(String idUser,T object) {
		MessageBrokerSingleton.getInstance().broadcast(id, idUser, object);
	}
	
	public Player findPlayerByUsername(String username) {
        for (Player player : players) {
            if (player.getUserName().equals(username)) {
                return player;
            }
        }
        return null;
    }

	
	@Override
	public void startGame() throws GameException, DatabaseConnectionException, UserException {
		currentTurn = Turn.builder()
                .player(players.get(0))
                .indexTurn(1)
                .build();
		currentTurn.numberOfTroopsCalculation(currentTurn.getPlayer().getTerritories());
		GameRepository.getInstance().insertTurn(currentTurn);		
	}
	
	@Override
	public void changeTurn() throws GameException, DatabaseConnectionException, UserException {
		Player curr = currentTurn.getPlayer();
		LOGGER.info("Cambio turno - giocatore corrente {}", curr.getUserName());
        setCurrentTurn(Turn.builder()
                .player(players.get((players.indexOf(curr) + 1) % players.size()))
                .indexTurn(currentTurn.getIndexTurn() + 1)
                .build());
        currentTurn.numberOfTroopsCalculation(currentTurn.getPlayer().getTerritories());
        GameRepository.getInstance().insertTurn(currentTurn);
       
        //broadcast();Chiamo change turn dallo stato fine turno con la action, alla fine della action faccio broadcast
	}
}