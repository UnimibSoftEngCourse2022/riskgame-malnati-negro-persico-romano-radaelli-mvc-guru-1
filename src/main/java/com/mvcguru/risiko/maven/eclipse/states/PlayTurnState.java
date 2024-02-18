package com.mvcguru.risiko.maven.eclipse.states;


import java.io.IOException;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.controller.bodyRequest.DefenderNoticeBody;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class PlayTurnState extends GameState{
	
	AttackRequest attackRequest;
	
	public void onActionPlayer(ComboRequest comboRequest) throws IOException {
		game.getCurrentTurn().setComboCards(comboRequest.getComboRequestBody().getComboCards());
		game.getCurrentTurn().numberOfTroopsCalculation(
				game.findPlayerByUsername(
						comboRequest.getComboRequestBody().getUsername()).getTerritories());
	} 
	
	public void onActionPlayer(TerritorySetup action) {
		//game.getCurrentTurn().getPlayer().setTerritories(action.getSetUpBody().getTerritories());
		game.broadcast();
	}
	
	public void onActionPlayer(AttackRequest attackRequest) {
		this.attackRequest = attackRequest;
		DefenderNoticeBody defenderNoticeBody = DefenderNoticeBody.builder()
                .idAttackerUser(attackRequest.getRequestAttackBody().getAttackerTerritory().getIdOwner())
                .numAttDice(attackRequest.getRequestAttackBody().getNumDice())
                .build();
		game.broadcast(attackRequest.getRequestAttackBody().getIdGame(), 
				attackRequest.getRequestAttackBody().getDefenderTerritory().getIdOwner(), 
				defenderNoticeBody);
	}
	
	public void onActionPlayer(DefenderNoticeBody defenderNoticeBody) {
		game.getCurrentTurn().attack(attackRequest.getRequestAttackBody(), defenderNoticeBody);
	}
	
	
}
