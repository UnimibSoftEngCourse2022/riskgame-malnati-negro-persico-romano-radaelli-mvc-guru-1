package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.actions.ActionPlayer;
import com.mvcguru.risiko.maven.eclipse.controller.MessageBrokerSingleton;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.deck.ObjectivesDeck;
import com.mvcguru.risiko.maven.eclipse.model.deck.TerritoriesDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Game extends IGame {
    
    //private transient LinkedList<GameState> stackStati = new LinkedList<>();
	
    
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
        //TODO scelta colore armate
    }

	@Override
	public void onActionPlayer(ActionPlayer action) throws FullGameException {
		action.accept(state);
		broadcast();
	}

	@Override
    public void broadcast() {
		LOGGER.info("Broadcasting", this);
        MessageBrokerSingleton.getInstance().broadcast(this);
	}
	
	public Player findPlayerByUsername(String username) {
        for (Player player : players) {
            if (player.getUserName().equals(username)) {
                return player;
            }
        }
        return null;
    }

	public TerritoriesDeck createTerritoryDeck(GameConfiguration configuration) throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("territories_difficult.json").getInputStream());
        String json = new String(data, StandardCharsets.UTF_8);
        TerritoryCard[] territoriesCard = mapper.readValue(json, TerritoryCard[].class);
        
        TerritoriesDeck deck = new TerritoriesDeck();
		for (TerritoryCard territoryCard : territoriesCard) {
	            deck.insertCard(territoryCard);
			}
		return deck;
        }

	public ObjectivesDeck createObjectiveDeck(GameConfiguration configuration) throws IOException {
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
        LOGGER.info("objectives: {}", objectives.toString());
        ObjectivesDeck deck = new ObjectivesDeck();
		for (ObjectiveCard o : objectives) {
            deck.insertCard(o);
        }
		return deck;
	}
}