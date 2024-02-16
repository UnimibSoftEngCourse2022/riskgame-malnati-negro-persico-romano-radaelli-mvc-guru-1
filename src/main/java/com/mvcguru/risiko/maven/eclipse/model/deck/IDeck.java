package com.mvcguru.risiko.maven.eclipse.model.deck;

import com.mvcguru.risiko.maven.eclipse.model.card.ICard;

public interface IDeck{
	
	public ICard drawCard();
	
	public void insertCard(ICard card);
	
	public void shuffle();
}
