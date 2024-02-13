package com.mvcguru.risiko.maven.eclipse.model.deck;

import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;

public interface IDeck {
	
	public ICard drawObjectiveCard();
	
	public ICard drawTerritoryCard();

}
