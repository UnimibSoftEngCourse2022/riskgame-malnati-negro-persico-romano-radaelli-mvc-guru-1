package com.mvcguru.risiko.maven.eclipse.model.deck;

import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;


public interface IDeck {
	
	public ICard drawObjectiveCard();
	
	public ICard drawTerritoryCard();
	
	public void insertTerritoryCard(TerritoryCard card);
	
	public void insertObjectiveCard(ObjectiveCard card);

	
	//private void shuffleDecks();
}
