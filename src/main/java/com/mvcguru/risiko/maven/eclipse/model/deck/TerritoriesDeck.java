package com.mvcguru.risiko.maven.eclipse.model.deck;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TerritoriesDeck implements IDeck{
	
    private Queue<TerritoryCard> cards = new LinkedList<>();

	@Override
	public ICard drawCard() {
		return cards.poll();
	}
	
	@Override
	public void insertCard(ICard card) {
		cards.add((TerritoryCard) card);
	}

	 @Override
	 public void shuffle() {
		 List<TerritoryCard> cardsList = new LinkedList<>(cards);
	     Collections.shuffle(cardsList);
	     cards.clear();
	     cards.addAll(cardsList);
	    }


}
