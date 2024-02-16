package com.mvcguru.risiko.maven.eclipse.model.card;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;

class ObjectiveCardTest {

    @Test
    void testNoArgsConstructor() {
        ObjectiveCard card = new ObjectiveCard();
        assertNotNull(card, "ObjectiveCard should be instantiated using no-args constructor");
    }

    @Test
    void testGettersAndSetters() {
        ObjectiveCard card = new ObjectiveCard();
        String expectedObjective = "Conquer all territories";
        card.setObjective(expectedObjective);

        assertEquals(expectedObjective, card.getObjective(), "Getter for objective should return what was set");
    }

    @Test
    void testEquals() {
        ObjectiveCard card1 = ObjectiveCard.builder().objective("Destroy all enemies").build();
        ObjectiveCard card2 = ObjectiveCard.builder().objective("Destroy all enemies").build();

        assertEquals(card1, card2, "Two cards with the same objective should be equal");
    }

    @Test
    void testHashCode() {
        ObjectiveCard card1 = ObjectiveCard.builder().objective("Capture 3 continents").build();
        ObjectiveCard card2 = ObjectiveCard.builder().objective("Capture 3 continents").build();

        assertEquals(card1.hashCode(), card2.hashCode(), "Hashcodes should match for equal ObjectiveCards");
    }

    @Test
    void testToString() {
        ObjectiveCard card = ObjectiveCard.builder().objective("Hold 18 territories with 2 troops on each").build();
        String toStringResult = card.toString();
        
        assertTrue(toStringResult.contains("Hold 18 territories with 2 troops on each"), "toString should contain the objective");
    }
}

