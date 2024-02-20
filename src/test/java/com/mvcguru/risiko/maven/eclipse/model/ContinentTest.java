package com.mvcguru.risiko.maven.eclipse.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContinentTest {

    private Continent continent;
    private List<Territory> territories;

    @BeforeEach
    void setUp() {
        continent = new Continent();

        territories = new ArrayList<>();
        territories.add(new Territory());
        territories.add(new Territory());
    }

    @Test
    void testGettersAndSetters() {
        continent.setContinentId(1);
        continent.setName("Africa");
        continent.setTerritories(territories);
        continent.setBonusArmies(3);
        
        assertEquals(1, continent.getContinentId());
        assertEquals("Africa", continent.getName());
        assertEquals(territories, continent.getTerritories());
        assertEquals(3, continent.getBonusArmies());
    }

    @Test
    void testNoArgsConstructor() {
        Continent newContinent = new Continent();
        
        assertNotNull(newContinent);
    }

    @Test
    void testSuperBuilder() {
        Continent builtContinent = Continent.builder()
                .continentId(2)
                .name("Asia")
                .territories(territories)
                .bonusArmies(7)
                .build();

        assertEquals(2, builtContinent.getContinentId());
        assertEquals("Asia", builtContinent.getName());
        assertEquals(territories, builtContinent.getTerritories());
        assertEquals(7, builtContinent.getBonusArmies());
    }
}
