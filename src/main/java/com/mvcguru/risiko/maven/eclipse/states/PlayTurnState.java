package com.mvcguru.risiko.maven.eclipse.states;


import java.io.IOException;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.DefenderNoticeBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryBody;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class PlayTurnState extends GameState{
	
	AttackRequest attackRequest;
	
	@Override
	public void onActionPlayer(ComboRequest comboRequest) throws IOException {
		game.getCurrentTurn().numberOfTroopsCalculation(
				game.findPlayerByUsername(
						comboRequest.getComboRequestBody().getUsername()).getTerritories()
				,comboRequest.getComboRequestBody().getComboCards());
	} 
	
	@Override
	public void onActionPlayer(TerritorySetup action) {
		for(TerritoryBody territory : action.getSetUpBody().getTerritories()) {
            game.getCurrentTurn().getPlayer().getTerritoryByName(territory.getName()).setArmies(territory.getTroops());
        }
	}
	
	@Override
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
	
	public void onActionPlayer(int numTroops) {
		game.getCurrentTurn().moveTroops(numTroops);
		game.broadcast();
	}
	
	
}
