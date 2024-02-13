package com.mvcguru.risiko.maven.eclipse.model.deck;

import java.util.Collections;
import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;

import lombok.Data;

@Data
public class Deck implements IDeck{
	
	private List<TerritoryCard> territoryCards;
	
	private List<ObjectiveCard> objectiveCards;


	//Mescola entrambi i mazzi di carte
    private void shuffleDecks() {
        Collections.shuffle(territoryCards);
        Collections.shuffle(objectiveCards);
    }

    @Override
    public ICard drawObjectiveCard() {
            return objectiveCards.remove(0);
    }

    @Override
    public ICard drawTerritoryCard() {
        if (!territoryCards.isEmpty()) {
            return territoryCards.remove(0);
        }
        return null; // Gestire il caso in cui non ci sono più carte territorio
    }



    // Metodo per reinserire una carta obiettivo nel mazzo (e mescolare nuovamente)
    public void insertObjectiveCard(ObjectiveCard card) {
        objectiveCards.add(card);
        //Collections.shuffle(objectiveCards);
    }

	@Override
	public void insertTerritoryCard(TerritoryCard card) {
		territoryCards.add(card);
        //Collections.shuffle(territoryCards);
	}

	

}

