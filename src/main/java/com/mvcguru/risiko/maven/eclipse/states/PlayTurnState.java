package com.mvcguru.risiko.maven.eclipse.states;


import java.io.IOException;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.DefenderNoticeBody;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class PlayTurnState extends GameState{
	
//	AttackRequest attackRequest;
//	
//		@Override
//		public void onActionPlayer(ComboRequest comboRequest) throws IOException {
//			game.getCurrentTurn().setComboCards(comboRequest.getComboRequestBody().getComboCards());
//			game.getCurrentTurn().numberOfTroopsCalculation(
//					game.findPlayerByUsername(
//							comboRequest.getComboRequestBody().getUsername()).getTerritories());
//		} 
//	
//	@Override
//	public void onActionPlayer(TerritorySetup action) {
//		game.broadcast();
//	}
//	
//	@Override
//	public void onActionPlayer(AttackRequest attackRequest) {
//		this.attackRequest = attackRequest;
//		DefenderNoticeBody defenderNoticeBody = DefenderNoticeBody.builder()
//                .idAttackerUser(attackRequest.getRequestAttackBody().getAttackerTerritory().getIdOwner())
//                .numAttDice(attackRequest.getRequestAttackBody().getNumDice())
//                .build();
//		game.broadcast(attackRequest.getRequestAttackBody().getIdGame(), 
//				attackRequest.getRequestAttackBody().getDefenderTerritory().getIdOwner(), 
//				defenderNoticeBody);
//	}
//	
//	public void onActionPlayer(DefenderNoticeBody defenderNoticeBody) {
//		game.getCurrentTurn().attack(attackRequest.getRequestAttackBody(), defenderNoticeBody);
//	}
//	
//	
}
