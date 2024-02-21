package com.mvcguru.risiko.maven.eclipse.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.DefenceRequest;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class BattleState extends GameState{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BattleState.class);

	@Override
	public void onActionPlayer(AttackRequest attackRequest) {
		LOGGER.info("attackRequest.getRequestAttackBody()", attackRequest.getRequestAttackBody());
		LOGGER.info("Numero dadi attacco: {}", attackRequest.getRequestAttackBody().getNumAttDice());
		
		LOGGER.info("Territorio attaccante: {}", attackRequest.getRequestAttackBody().getAttackerTerritory().getNameTerritory());
		for (Territory t : attackRequest.getPlayer().getTerritories()) {
			LOGGER.info("Territorio action player: {}", t.getName());
		}
		game.getCurrentTurn().setNumAttDice(attackRequest.getRequestAttackBody().getNumAttDice());
		LOGGER.info("Numero dadi attacco: {}", game.getCurrentTurn().getNumAttDice());
		LOGGER.info("Turno corrente: {}", game.getCurrentTurn().getAttackerTerritory());
		game.getCurrentTurn().setAttackerTerritory(
				attackRequest.getPlayer().getTerritoryByName(attackRequest.getRequestAttackBody().getAttackerTerritory().getNameTerritory()));
		LOGGER.info("Turno corrente: {}", game.getCurrentTurn().getAttackerTerritory());

		game.getCurrentTurn().setDefenderTerritory(
				game.findPlayerByUsername(attackRequest.getRequestAttackBody().getDefenderTerritory().getUsername()).getTerritoryByName(attackRequest.getRequestAttackBody().getDefenderTerritory().getNameTerritory()));
		LOGGER.info("Territorio attaccante: {}", game.getCurrentTurn().getAttackerTerritory().getName());
		LOGGER.info("Territorio difensore: {}", game.getCurrentTurn().getDefenderTerritory().getName());
	
		try {
			GameRepository.getInstance().updateNumAttackDice(game.getCurrentTurn(), game.getCurrentTurn().getNumAttDice());
			LOGGER.info("Numero dadi attacco aggiornato nel database");
			GameRepository.getInstance().updateAttackerTerritory(game.getCurrentTurn(), game.getCurrentTurn().getAttackerTerritory());
			LOGGER.info("Territorio attaccante aggiornato nel database");
			GameRepository.getInstance().updateDefenderTerritory(game.getCurrentTurn(), game.getCurrentTurn().getDefenderTerritory());
			LOGGER.info("Territorio difensore aggiornato nel database");
		} catch (GameException | DatabaseConnectionException | UserException e) {
			LOGGER.error("Errore nell'aggiornamento dei dadi dell'attacco");
		}
	}
	
	@Override
	public void onActionPlayer(DefenceRequest defenceRequest) throws GameException, DatabaseConnectionException, UserException {
		game.getCurrentTurn().setNumDefDice(defenceRequest.getDefenderRequestBody().getNumDefDice());
		GameRepository.getInstance().updateNumDefenseDice(game.getCurrentTurn(), game.getCurrentTurn().getNumDefDice());
		game.getCurrentTurn().attack();
	}
	
	//@Override
	public void onActionPlayer(int numTroops) {
		game.getCurrentTurn().moveTroops(numTroops);
		game.broadcast();
	}
	
}
