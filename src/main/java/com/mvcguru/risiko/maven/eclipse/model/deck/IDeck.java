package com.mvcguru.risiko.maven.eclipse.model.deck;

import java.io.Serializable;

import com.mvcguru.risiko.maven.eclipse.model.card.ICard;

public interface IDeck extends Serializable{
	
	public ICard drawCard();
	
	public void insertCard(ICard card);
	
	public void shuffle();
}
