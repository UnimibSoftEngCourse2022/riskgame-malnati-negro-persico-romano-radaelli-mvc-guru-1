package com.mvcguru.risiko.maven.eclipse.model.deck;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.ObjectiveCard;

public class ObjectivesDeck implements IDeck{
	
    private Queue<ObjectiveCard> cards = new LinkedList<>();

	@Override
	public ICard drawCard() {
		return cards.poll();
	}

	@Override
	public void insertCard(ICard card) {
		cards.add((ObjectiveCard) card);
	}

	@Override
	public void shuffle() {
		List<ObjectiveCard> cardsList = new LinkedList<>(cards);
	     Collections.shuffle(cardsList);
	     cards.clear();
	     cards.addAll(cardsList);
	}

}
