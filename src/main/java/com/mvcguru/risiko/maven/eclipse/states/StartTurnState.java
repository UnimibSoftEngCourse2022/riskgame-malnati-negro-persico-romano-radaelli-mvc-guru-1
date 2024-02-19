package com.mvcguru.risiko.maven.eclipse.states;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.actions.TurnSetUp;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryCardBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;
import com.mvcguru.risiko.maven.eclipse.service.database.DaoSQLiteImpl;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class StartTurnState extends GameState{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoSQLiteImpl.class);

	
	@Override
	public void onActionPlayer(ComboRequest comboRequest) {
		List<TerritoryCard> result = new ArrayList<TerritoryCard>();
		for (TerritoryCardBody t : comboRequest.getComboRequestBody().getComboCards()) {
			result.add(comboRequest.getPlayer().getComboCards().stream()
                    .filter(card -> card.getTerritory().getName().equals(t.getName()))
                    .findFirst().get());
		}
		if(!result.isEmpty()) {
			for (TerritoryCard t : result) {
				comboRequest.getPlayer().getComboCards().remove(t);
				try {
					GameRepository.getInstance().deleteComboCard(t, comboRequest.getPlayer(), game.getId());
				} catch (GameException | DatabaseConnectionException | UserException e) {
					LOGGER.error("Errore nella rimozione della carta combo");
				}
			}
		}
		//List <TerritoryCard> comboCards = comboRequest.ge
		game.getCurrentTurn().comboCardsCheck(result);
	}
	
	@Override
	public void onActionPlayer(TurnSetUp turnSetUp) throws GameException, DatabaseConnectionException, UserException {
		Player player = turnSetUp.getPlayer();
		LOGGER.info("Turno di {}", player.getUserName());
		for(TerritoryBody t : turnSetUp.getSetUpBody().getTerritories()) {
			String territoryName = t.getName();
			int troops = t.getTroops();
			player.getTerritories().stream().filter(territory -> territory.getName().equals(territoryName)).findFirst()
					.ifPresent(territory -> {
						territory.setArmies(troops);
						try {
							GameRepository.getInstance().updateTerritoryArmies(territory.getName(), territory.getArmies());
						} catch (GameException | DatabaseConnectionException | UserException e) {
							LOGGER.error("Errore nell'aggiornamento delle truppe del territorio {}", territory.getName());
						}
						LOGGER.info("Territorio {} con truppe  {}", territory.getName(), territory.getArmies());
					});
		}
		
		LOGGER.info("fine action");
		
		game.setState(BattleState.builder().game(game).build());
		GameRepository.getInstance().updateState(game.getId(), game.getState());
		
	}
}
