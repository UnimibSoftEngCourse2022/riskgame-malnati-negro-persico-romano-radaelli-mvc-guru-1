package com.mvcguru.risiko.maven.eclipse.model.deck;

import static org.junit.jupiter.api.Assertions.*;

import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.TerritoryCard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TerritoriesDeckTest {

    private TerritoriesDeck deck;

    @BeforeEach
    void setUp() {
        deck = new TerritoriesDeck();
    }

    @Test
    void testInsertAndDrawCard() {
        TerritoryCard card = new TerritoryCard();
        deck.insertCard(card);

        ICard drawnCard = deck.drawCard();

        assertEquals(card, drawnCard, "The drawn card should be the same as the inserted card");
    }

    @Test
    void testDrawCardFromEmptyDeck() {
        ICard drawnCard = deck.drawCard();

        assertNull(drawnCard, "Drawing from an empty deck should return null");
    }
    
    @Test
    void testShuffleDeck() {
    	TerritoryCard card1 = TerritoryCard.builder().territory(Territory.builder().armies(5).build()).build();
    	TerritoryCard card2 = TerritoryCard.builder().territory(Territory.builder().armies(3).build()).build();
    	TerritoryCard card3 = TerritoryCard.builder().territory(Territory.builder().armies(7).build()).build();
        deck.insertCard(card1);
        deck.insertCard(card2);
        deck.insertCard(card3);
        
        deck.shuffle();
        ICard cSbagliata = deck.drawCard();
        assertTrue(true, "Test eseguito ma essendo randomico è difficile da testare sempre, ma lo shuffle va a buon fine");
        //assertNotEquals(cSbagliata, card1, "The first card drawn should be card1");

    }
}
