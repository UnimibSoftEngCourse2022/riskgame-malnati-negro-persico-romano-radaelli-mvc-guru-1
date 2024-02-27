package com.mvcguru.risiko.maven.eclipse.states;

import java.io.IOException;

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
		LOGGER.info("3------ AttackRequest {}", attackRequest.toString());
		
		Territory attacker = GameRepository.getInstance().getTerritory(attackRequest.getRequestAttackBody().getAttackerTerritory().getNameTerritory(), attackRequest.getPlayer().getUserName(), game.getId());
		Territory defender = GameRepository.getInstance().getTerritory(attackRequest.getRequestAttackBody().getDefenderTerritory().getNameTerritory(), attackRequest.getRequestAttackBody().getDefenderTerritory().getUsername(), game.getId());
		
		game.getCurrentTurn().setDefenderTerritory(defender);
		
		GameRepository.getInstance().updateNumAttackDice(game.getCurrentTurn(), attackRequest.getRequestAttackBody().getNumAttDice());		
		GameRepository.getInstance().updateAttackerTerritory(game.getCurrentTurn(), attacker);
		GameRepository.getInstance().updateDefenderTerritory(game.getCurrentTurn(), defender);
	}
	
	@Override
	public void onActionPlayer(DefenceRequest defenceRequest) throws GameException, DatabaseConnectionException, UserException, IOException {
		LOGGER.info("5------ DefenceRequest {}", defenceRequest.toString());
		
    	Territory attacker = GameRepository.getInstance().getTerritory(game.getCurrentTurn().getAttackerTerritory().getName(), game.getCurrentTurn().getPlayer().getUserName(), game.getId());
		Territory defender = GameRepository.getInstance().getTerritory(game.getCurrentTurn().getDefenderTerritory().getName(), defenceRequest.getPlayer().getUserName(), game.getId());
		
		game.getCurrentTurn().setAttackerTerritory(attacker);	
		game.getCurrentTurn().setDefenderTerritory(defender);
		game.getCurrentTurn().setNumAttDice(GameRepository.getInstance().getTurn(game.getId(), game.getCurrentTurn().getIndexTurn()).getNumAttDice());
		game.getCurrentTurn().setNumDefDice(defenceRequest.getDefenderRequestBody().getNumDefDice());
		
		GameRepository.getInstance().updateNumDefenseDice(game.getCurrentTurn(), game.getCurrentTurn().getNumDefDice());
		
		game.getCurrentTurn().attack();
		
		System.out.println("attacco finito ");
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
