package com.mvcguru.risiko.maven.eclipse.model.player;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.player.Player.PlayerColor;
import com.mvcguru.risiko.maven.eclipse.model.IGame;

class PlayerTest {

    @Test
    void testNoArgsConstructor() {
        Player player = new Player();
        assertNotNull(player, "Player should be instantiated using no-args constructor");
    }

    @Test
    void testAllArgsConstructor() {
        String userName = "TestUser";
        String gameId = "Game123";
        PlayerColor color = PlayerColor.BLUE;
        IGame game = null; 
        List<Territory> territories = new ArrayList<>();
        List<TerritoryCard> comboCards = new ArrayList<>();
        ObjectiveCard objective = null; 
		Player player = Player.builder().userName(userName).gameId(gameId).color(color).game(game)
				.territories(territories).comboCards(comboCards).objective(objective).build();

        assertEquals(userName, player.getUserName(), "UserName should match constructor argument");
        assertEquals(gameId, player.getGameId(), "GameId should match constructor argument");
        assertEquals(color, player.getColor(), "Color should match constructor argument");
        assertNull(player.getGame(), "Game should match constructor argument (null)");
        assertEquals(territories, player.getTerritories(), "Territories should match constructor argument");
        assertEquals(comboCards, player.getComboCards(), "ComboCards should match constructor argument");
        assertNull(player.getObjective(), "Objective should match constructor argument (null)");
    }

    @Test
    void testBuilder() {
        String userName = "BuilderUser";
        String gameId = "Game456";
        PlayerColor color = PlayerColor.GREEN;
        List<Territory> territories = new ArrayList<>();
        List<TerritoryCard> comboCards = new ArrayList<>();
        ObjectiveCard objective = null; 

        Player player = Player.builder()
                              .userName(userName)
                              .gameId(gameId)
                              .color(color)
                              .game(null)
                              .territories(territories)
                              .comboCards(comboCards)
                              .objective(objective)
                              .build();

        assertEquals(userName, player.getUserName(), "Builder should set userName correctly");
        assertEquals(gameId, player.getGameId(), "Builder should set gameId correctly");
        assertEquals(color, player.getColor(), "Builder should set color correctly");
        assertNull(player.getGame(), "Builder should handle game correctly");
        assertEquals(territories, player.getTerritories(), "Builder should set territories correctly");
        assertEquals(comboCards, player.getComboCards(), "Builder should set comboCards correctly");
        assertNull(player.getObjective(), "Builder should handle objective correctly");
    }

    @Test
    void testEqualsAndHashCode() {
        Player player1 = Player.builder().userName("User1").build();
        Player player2 = Player.builder().userName("User1").build();

        assertEquals(player1, player2, "Two players with the same userName should be considered equal");
        assertEquals(player1.hashCode(), player2.hashCode(), "HashCodes should match for two equal Players");
    }

    @Test
    void testToString() {
        Player player = Player.builder().userName("ToStringTest").color(PlayerColor.RED).build();

        String toStringResult = player.toString();
        assertTrue(toStringResult.contains("ToStringTest"), "toString should contain the userName");
        assertTrue(toStringResult.contains("RED"), "toString should contain the color");
    }
    
    @Test
    void getTerritoryByName_ShouldReturnCorrectTerritory_WhenNameMatches() {
    	Territory territory1 = new Territory();
        territory1.setName("Territory1");
        
        Territory territory2 = new Territory();
        territory2.setName("Territory2");

        Player player = Player.builder()
                .territories(Arrays.asList(territory1, territory2))
                .build();
        
        Territory foundTerritory = player.getTerritoryByName("Territory1");
        assertEquals(territory1, foundTerritory, "The method should return the correct territory when the name matches.");
    }

    @Test
    void getTerritoryByName_ShouldReturnNull_WhenNoMatchFound() {
    	Territory territory1 = new Territory();
        territory1.setName("Territory1");
        
        Territory territory2 = new Territory();
        territory2.setName("Territory2");

        Player player = Player.builder()
                .territories(Arrays.asList(territory1, territory2))
                .build();
    	
        Territory foundTerritory = player.getTerritoryByName("NonExistentTerritory");
        assertNull(foundTerritory, "The method should return null when no matching territory name is found.");
    }
}
