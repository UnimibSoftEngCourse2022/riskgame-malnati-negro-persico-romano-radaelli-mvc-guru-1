package com.mvcguru.risiko.maven.eclipse.model.deck;

import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;


public interface IDeck{
	
	public ICard drawCard();
	
	public void insertCard(ICard card);
	
	public void shuffle();
}
