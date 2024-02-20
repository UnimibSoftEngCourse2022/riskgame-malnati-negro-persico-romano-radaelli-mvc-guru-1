package com.mvcguru.risiko.maven.eclipse.states;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.DefenceRequest;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class BattleState extends GameState{

	@Override
	public void onActionPlayer(AttackRequest attackRequest) {
		game.getCurrentTurn().setNumAttDice(attackRequest.getRequestAttackBody().getNumAttDice());
		game.getCurrentTurn().setAttackerTerritory(
				attackRequest.getPlayer().getTerritoryByName(attackRequest.getRequestAttackBody().getAttackerTerritory().getNameTerritory()));
		game.getCurrentTurn().setDefenderTerritory(
				game.findPlayerByUsername(attackRequest.getRequestAttackBody().getDefenderTerritory().getUsername()).getTerritoryByName(attackRequest.getRequestAttackBody().getDefenderTerritory().getNameTerritory()));
	}
	
	@Override
	public void onActionPlayer(DefenceRequest defenceRequest) throws GameException, DatabaseConnectionException, UserException {
		game.getCurrentTurn().setNumDefDice(defenceRequest.getDefenderRequestBody().getNumDefDice());
		game.getCurrentTurn().attack();
	}
	
	//@Override
	public void onActionPlayer(int numTroops) {
		game.getCurrentTurn().moveTroops(numTroops);
		game.broadcast();
	}
	
}
