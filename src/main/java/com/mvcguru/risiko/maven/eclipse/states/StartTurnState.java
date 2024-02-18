package com.mvcguru.risiko.maven.eclipse.states;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.actions.TurnSetUp;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryCardBody;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;


public class StartTurnState extends GameState{
	
	@Override
	public void onActionPlayer(ComboRequest comboRequest) throws IOException {
		List<TerritoryCard> result = new ArrayList<TerritoryCard>();
		for (TerritoryCardBody t : comboRequest.getComboRequestBody().getComboCards()) {
			result.add(comboRequest.getPlayer().getComboCards().stream()
                    .filter(card -> card.getTerritory().getName().equals(t.getName()))
                    .findFirst().get());
		}
		if(!result.isEmpty()) {
			for (TerritoryCard t : result) {
				comboRequest.getPlayer().getComboCards().remove(t);
				//Delete database
			}
		}
		//List <TerritoryCard> comboCards = comboRequest.ge
		game.getCurrentTurn().comboCardsCheck(result);
		game.getCurrentTurn().numberOfTroopsCalculation(comboRequest.getPlayer().getTerritories());
	}
	
	@Override
	public void onActionPlayer(TurnSetUp turnSetUp) {
		//game.broadcast();
	}
}
