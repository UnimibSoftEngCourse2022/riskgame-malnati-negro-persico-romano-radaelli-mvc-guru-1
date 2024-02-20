package com.mvcguru.risiko.maven.eclipse.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


class TerritoryTest {

	@Test
    void testNoArgsConstructor() {
        Territory territory = new Territory();
        assertNotNull(territory, "Territory should be instantiated using no-args constructor");
    }

    @Test
    void testGettersAndSetters() {
        Territory territory = new Territory();
        territory.setContinent(1);
        territory.setName("TerritoryName");
        territory.setArmies(5);
        
        
        territory.setIdOwner("idOwner");

        List<String> neighbors = new ArrayList<>();
        neighbors.add(new String()); 
        territory.setNeighbors(neighbors);

        assertEquals(1, territory.getContinent(), "Continent getter should return what was set");
        assertEquals("TerritoryName", territory.getName(), "Name getter should return what was set");
        assertEquals(5, territory.getArmies(), "Armies getter should return what was set");
        assertSame("idOwner", territory.getIdOwner(), "Owner getter should return what was set");
        assertEquals(neighbors, territory.getNeighbors(), "Neighbors getter should return what was set");
    }

    @Test
    void testBuilder() {
        List<String> neighbors = new ArrayList<>();
        neighbors.add(new String()); 

        Territory territory = Territory.builder()
                                        .continent(2)
                                        .name("AnotherTerritory")
                                        .armies(10)
                                        .idOwner("idOwner")
                                        .neighbors(neighbors)
                                        .build();

        assertEquals(2, territory.getContinent(), "Builder should set continent correctly");
        assertEquals("AnotherTerritory", territory.getName(), "Builder should set name correctly");
        assertEquals(10, territory.getArmies(), "Builder should set armies correctly");
        assertSame("idOwner", territory.getIdOwner(), "Builder should set owner correctly");
        assertEquals(neighbors, territory.getNeighbors(), "Builder should set neighbors correctly");
    }

    @Test
    void testEquals() {
        Territory territory1 = new Territory();
        territory1.setName("Territory1");
        
        Territory territory2 = new Territory();
        territory2.setName("Territory1");

        assertEquals(territory1, territory2, "Two territories with the same name should be considered equal");
    }

    @Test
    void testHashCode() {
        Territory territory1 = new Territory();
        territory1.setName("Territory1");
        
        Territory territory2 = new Territory();
        territory2.setName("Territory1");

        assertEquals(territory1.hashCode(), territory2.hashCode(), "HashCodes should match for equal territories");
    }

    @Test
    void testToString() {
        Territory territory = Territory.builder()
                                        .name("TestTerritory")
                                        .build();

        String toStringResult = territory.toString();
        assertTrue(toStringResult.contains("TestTerritory"), "toString should contain the territory name");
    }
}
