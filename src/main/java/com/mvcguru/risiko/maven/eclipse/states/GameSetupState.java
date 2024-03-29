package com.mvcguru.risiko.maven.eclipse.states;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.deck.IDeck;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class GameSetupState extends GameState {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GameSetupState.class);

	@Override
	public void onActionPlayer(TerritorySetup action) throws GameException, DatabaseConnectionException, UserException, FullGameException, IOException{
		Player player = action.getPlayer();
		 
		for (TerritoryBody t : action.getSetUpBody().getTerritories()) {
		    String territoryName = t.getName();
		    int troops = t.getTroops();

		    player.getTerritories().stream()
		          .filter(territory -> territory.getName().equals(territoryName))
		          .forEach(territory -> {
		              territory.setArmies(troops);
		              try {
		                  GameRepository.getInstance().updateTerritoryArmies(territory.getName(), game.getId(), territory.getArmies());
		              } catch (GameException | DatabaseConnectionException | UserException e) {
		                  LOGGER.error("Errore nell'aggiornamento delle truppe del territorio {}", territory.getName());
		              }
		              LOGGER.info("Territorio {} con truppe {}", territory.getName(), territory.getArmies());
		          });
		}
		
		player.setSetUpCompleted(true); 
		GameRepository.getInstance().updateSetUpCompleted(action.getPlayer().getUserName(), true);

		for (Player p : GameRepository.getInstance().getAllPlayers(game.getId())) {
			if (!p.isSetUpCompleted()) {
				return;
			}
		}
		game.startGame(); 
		game.setState(StartTurnState.builder().game(game).build());
		GameRepository.getInstance().updateState(game.getId(), game.getState());
		
	}
	
	@Override
	public void setUpGame() throws GameException, DatabaseConnectionException, UserException {
		assignColor(game.getPlayers());
		assignTerritories(game.getDeckTerritory());
		assignObjective(game.getDeckObjective());
	}

	private void assignObjective(IDeck deckObjective) throws GameException, DatabaseConnectionException, UserException {
		deckObjective.shuffle();
		ObjectiveCard card = null;
			
		for (Player player : game.getPlayers()) {
			card = (ObjectiveCard) deckObjective.drawCard();
			player.setObjective(card);
			GameRepository.getInstance().updateObjective(player.getUserName(), card.getObjective());
		    if(game.getConfiguration().getMode().name().equals("EASY"))
		        deckObjective.insertCard(card);
		        
		}

	}
	
	private void assignTerritories(IDeck deckTerritory) throws GameException, DatabaseConnectionException, UserException {
	    deckTerritory.shuffle();
	    int playerIndex = 0;
	    TerritoryCard card = (TerritoryCard)deckTerritory.drawCard();
	    while (card != null) {
	        if (card.getSymbol().equals(CardSymbol.JOLLY)) {
	            deckTerritory.insertCard(card);
	            deckTerritory.shuffle();
	        } else {
	            game.getPlayers().get(playerIndex % game.getPlayers().size()).getTerritories().add(card.getTerritory());
	            card.getTerritory().setIdOwner(game.getPlayers().get(playerIndex % game.getPlayers().size()).getUserName());
	            GameRepository.getInstance().insertTerritory(card.getTerritory(), game.getId());
	            playerIndex++;
	        }
	        card = (TerritoryCard) deckTerritory.drawCard();
	    }
	}

	private void assignColor(List<Player> players) throws GameException, DatabaseConnectionException, UserException {
		List<PlayerColor> colors = new ArrayList<>(Arrays.asList(PlayerColor.values()));
		colors.remove(PlayerColor.GREY);
		Collections.shuffle(colors);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setColor(colors.get(i));
			GameRepository.getInstance().updatePlayerColor(players.get(i));
		}
		
	}
}
