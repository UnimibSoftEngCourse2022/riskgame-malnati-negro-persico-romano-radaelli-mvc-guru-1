package com.mvcguru.risiko.maven.eclipse.states;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.model.Territory;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class PlayTurnState extends GameState{
	
	public void onActionPlayer(ComboRequest comboRequest) {
		game.getCurrentTurn().setComboCards(comboRequest.getRequestComboBody().getComboCards());
		game.getCurrentTurn().numberOfTroopsCalculation(
				game.findPlayerByUsername(
						comboRequest.getRequestComboBody().getUsername()).getTerritories());
	} 
	
	public void onActionPlayer(TerritorySetup action) {
		game.getCurrentTurn().getPlayer().setTerritories(action.getSetUpBody().getTerritories());
		game.broadcast();
	}
	
}
