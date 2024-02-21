package com.mvcguru.risiko.maven.eclipse.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ConquerAssignment;
import com.mvcguru.risiko.maven.eclipse.actions.DefenceRequest;
import com.mvcguru.risiko.maven.eclipse.actions.GoToEndTurn;
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
	public void onActionPlayer(AttackRequest attackRequest) throws GameException, DatabaseConnectionException, UserException {
		
		game.getCurrentTurn().setNumAttDice(attackRequest.getRequestAttackBody().getNumAttDice());
		game.getCurrentTurn().setAttackerTerritory(
				attackRequest.getPlayer().getTerritoryByName(attackRequest.getRequestAttackBody().getAttackerTerritory().getNameTerritory()));

		game.getCurrentTurn().setDefenderTerritory(

		game.findPlayerByUsername(attackRequest.getRequestAttackBody().getDefenderTerritory().getUsername()).getTerritoryByName(
				attackRequest.getRequestAttackBody().getDefenderTerritory().getNameTerritory()));
	
		
		GameRepository.getInstance().updateNumAttackDice(game.getCurrentTurn(), game.getCurrentTurn().getNumAttDice());
		GameRepository.getInstance().updateAttackerTerritory(game.getCurrentTurn(), game.getCurrentTurn().getAttackerTerritory());
		GameRepository.getInstance().updateDefenderTerritory(game.getCurrentTurn(), game.getCurrentTurn().getDefenderTerritory());
	}
	
	@Override
	public void onActionPlayer(DefenceRequest defenceRequest) throws GameException, DatabaseConnectionException, UserException {
		game.getCurrentTurn().setNumDefDice(defenceRequest.getDefenderRequestBody().getNumDefDice());
		GameRepository.getInstance().updateNumDefenseDice(game.getCurrentTurn(), game.getCurrentTurn().getNumDefDice());
		game.getCurrentTurn().attack();
	}
	
	@Override
	public void onActionPlayer(ConquerAssignment conquerAssignment) throws GameException, DatabaseConnectionException, UserException {
		game.getCurrentTurn().moveTroops(conquerAssignment.getNumTroops());
	}
	
	@Override
	public void onActionPlayer(GoToEndTurn goToEndTurn) throws GameException, DatabaseConnectionException, UserException {
		game.setState(EndTurnState.builder().game(game).build());
		GameRepository.getInstance().updateState(game.getId(), game.getState());
	}
	
}
