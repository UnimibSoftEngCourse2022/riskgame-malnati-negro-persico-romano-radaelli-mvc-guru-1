package com.mvcguru.risiko.maven.eclipse.states;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.DefenceRequest;

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
				attackRequest.getPlayer().getTerritoryByName(attackRequest.getRequestAttackBody().getDefenderTerritory().getNameTerritory()));
	}
	
	@Override
	public void onActionPlayer(DefenceRequest defenceRequest) {
		game.getCurrentTurn().setNumDefDice(defenceRequest.getDefenderRequestBody().getNumDefDice());
		game.getCurrentTurn().attack();
	}
	
	//@Override
	public void onActionPlayer(int numTroops) {
		game.getCurrentTurn().moveTroops(numTroops);
		game.broadcast();
	}
	
}
