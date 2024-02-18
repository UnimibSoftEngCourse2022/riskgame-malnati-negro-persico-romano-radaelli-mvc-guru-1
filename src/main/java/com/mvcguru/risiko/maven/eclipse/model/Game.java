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
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.deck.ObjectivesDeck;
import com.mvcguru.risiko.maven.eclipse.model.deck.TerritoriesDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.states.PlayTurnState;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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
	public void onActionPlayer(ActionPlayer action) throws FullGameException, IOException {
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

	public IDeck createTerritoryDeck(GameConfiguration configuration) throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("territories_difficult.json").getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        TerritoryCard[] territoriesCard = mapper.readValue(json, TerritoryCard[].class);
        
        IDeck deck = new TerritoriesDeck();
		for (TerritoryCard territoryCard : territoriesCard) {
	            deck.insertCard(territoryCard);
			}
		return deck;
        }

	public IDeck createObjectiveDeck(GameConfiguration configuration) throws IOException {
		GameMode mode = configuration.getMode();
		ObjectMapper mapper = new ObjectMapper();
		byte[] data = null;
		switch (mode) {
			case EASY:
				data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives_easy.json").getInputStream());
				break;
			case MEDIUM:
				data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives_medium.json").getInputStream());
				break;
            case HARD:
				data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives_hard.json").getInputStream());
				break;
			default:
				LOGGER.error("Game mode not found");
				break;
		}
        
        String json = new String(data, StandardCharsets.UTF_8);
        LOGGER.info("json: {}", json);
        ObjectiveCard[] objectives = mapper.readValue(json, ObjectiveCard[].class);
        IDeck deck = new ObjectivesDeck();
		for (ObjectiveCard o : objectives) {
            deck.insertCard(o);
        }
		return deck;
	}
	
	public void startGame() {
		currentTurn = Turn.builder()
                .player(players.get(0))
                .build();
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