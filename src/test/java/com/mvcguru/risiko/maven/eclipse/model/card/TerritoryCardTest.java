package com.mvcguru.risiko.maven.eclipse.model.card;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;

class TerritoryCardTest {

    @Test
    void testNoArgsConstructor() {
        TerritoryCard card = new TerritoryCard();
        assertNotNull(card, "TerritoryCard should be instantiated using no-args constructor");
    }

    @Test
    void testGettersAndSetters() {
        TerritoryCard card = new TerritoryCard();
        Territory territory = new Territory(); 
        card.setTerritory(territory);
        card.setSymbol(CardSymbol.INFANTRY);

        assertSame(territory, card.getTerritory(), "Getter for territory should return what was set");
        assertEquals(CardSymbol.INFANTRY, card.getSymbol(), "Getter for symbol should return what was set");
    }

    @Test
    void testEquals() {
        Territory territory1 = new Territory(); 
        TerritoryCard card1 = TerritoryCard.builder()
                                            .territory(territory1)
                                            .symbol(CardSymbol.CAVALRY)
                                            .build();

        Territory territory2 = new Territory();
        TerritoryCard card2 = TerritoryCard.builder()
                                            .territory(territory2)
                                            .symbol(CardSymbol.CAVALRY)
                                            .build();

        assertEquals(card1, card2, "Two cards with the same territory and symbol should be equal");
    }

    @Test
    void testHashCode() {
        Territory territory = new Territory();
        TerritoryCard card1 = TerritoryCard.builder()
                                            .territory(territory)
                                            .symbol(CardSymbol.ARTILLERY)
                                            .build();
        TerritoryCard card2 = TerritoryCard.builder()
                                            .territory(territory)
                                            .symbol(CardSymbol.ARTILLERY)
                                            .build();

        assertEquals(card1.hashCode(), card2.hashCode(), "Hashcodes should match for equal TerritoryCards");
    }

    @Test
    void testToString() {
        Territory territory = new Territory();
        TerritoryCard card = TerritoryCard.builder()
                                           .territory(territory)
                                           .symbol(CardSymbol.JOLLY)
                                           .build();

        String toStringResult = card.toString();
        assertTrue(toStringResult.contains("JOLLY"), "toString should contain the symbol");
        assertTrue(toStringResult.contains("territory"), "toString should mention territory");
    }
}
