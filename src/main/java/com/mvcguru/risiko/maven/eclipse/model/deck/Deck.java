package com.mvcguru.risiko.maven.eclipse.model.deck;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;

import lombok.Data;

@Data
public class Deck implements IDeck{
	
	private List<TerritoryCard> territoryCards;
	
	private List<ObjectiveCard> objectiveCards;

	@Override
	public ICard drawObjectiveCard() {
		return objectiveCards.remove(0);
	}

	@Override
	public ICard drawTerritoryCard() {
		return territoryCards.remove(0);
	}

	
}
