package com.mvcguru.risiko.maven.eclipse.model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mvcguru.risiko.maven.eclipse.controller.body_request.ResultNoticeBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TurnTest {

    private Turn turn;
    @Mock
    private Player player;
    @Mock
    private Game game;
    @Mock
    private Territory attackerTerritory, defenderTerritory;
    private List<Territory> territories;
    private List<Continent> continents;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        turn = new Turn();

        territories = new ArrayList<>();
        continents = new ArrayList<>();
        when(player.getGame()).thenReturn(game);
        when(game.getContinents()).thenReturn(continents);
        turn.setPlayer(player);

        when(attackerTerritory.getName()).thenReturn("AttackerTerritory");
        when(defenderTerritory.getName()).thenReturn("DefenderTerritory");
        turn.setAttackerTerritory(attackerTerritory);
        turn.setDefenderTerritory(defenderTerritory);

        when(player.getTerritories()).thenReturn(territories);
    }

    @Test
    void numberOfTroopsCalculation_ShouldCalculateCorrectly() {
        territories.add(new Territory());
        territories.add(new Territory());
        territories.add(new Territory());

        Continent continent = new Continent();
        continent.setBonusArmies(5);
        continent.setTerritories(territories);
        continents.add(continent);

        turn.numberOfTroopsCalculation(territories);

        assertTrue(turn.getNumberOfTroops() >= 1 + 5);
    }

    @Test
    void comboCardsCheck_ShouldCorrectlyAssignTroops_ForDifferentCombos() {
        List<TerritoryCard> comboCards = Arrays.asList(
        		TerritoryCard.builder().symbol(CardSymbol.ARTILLERY).build(),
                TerritoryCard.builder().symbol(CardSymbol.ARTILLERY).build(),
                TerritoryCard.builder().symbol(CardSymbol.ARTILLERY).build()
        );
        turn.comboCardsCheck(comboCards);
        assertEquals(4, turn.getNumberOfTroops());
        
        comboCards = Arrays.asList(
        		TerritoryCard.builder().symbol(CardSymbol.INFANTRY).build(),
                TerritoryCard.builder().symbol(CardSymbol.INFANTRY).build(),
                TerritoryCard.builder().symbol(CardSymbol.INFANTRY).build()
        );
        turn.comboCardsCheck(comboCards);
        assertEquals(6, turn.getNumberOfTroops());
        
        comboCards = Arrays.asList(
        		TerritoryCard.builder().symbol(CardSymbol.CAVALRY).build(),
                TerritoryCard.builder().symbol(CardSymbol.CAVALRY).build(),
                TerritoryCard.builder().symbol(CardSymbol.CAVALRY).build()
        );
        turn.comboCardsCheck(comboCards);
        assertEquals(8, turn.getNumberOfTroops());

        comboCards = Arrays.asList(
        		TerritoryCard.builder().symbol(CardSymbol.ARTILLERY).build(),
                TerritoryCard.builder().symbol(CardSymbol.CAVALRY).build(),
                TerritoryCard.builder().symbol(CardSymbol.INFANTRY).build()
        );
        turn.comboCardsCheck(comboCards);
        assertEquals(10, turn.getNumberOfTroops());

        comboCards = Arrays.asList(
        		TerritoryCard.builder().symbol(CardSymbol.JOLLY).build(),
                TerritoryCard.builder().symbol(CardSymbol.CAVALRY).build(),
            	TerritoryCard.builder().symbol(CardSymbol.INFANTRY).build()
        );
        turn.comboCardsCheck(comboCards);
        assertEquals(0, turn.getNumberOfTroops());
        
        comboCards = Arrays.asList(
        		TerritoryCard.builder().symbol(CardSymbol.JOLLY).build(),
                TerritoryCard.builder().symbol(CardSymbol.CAVALRY).build(),
            	TerritoryCard.builder().symbol(CardSymbol.CAVALRY).build()
        );
        turn.comboCardsCheck(comboCards);
        assertEquals(12, turn.getNumberOfTroops());
    }

//    @Test
//     void attack_ShouldCorrectlySimulateBattleAndConquer() {
//        when(player.getTerritoryByName("AttackerTerritory")).thenReturn(attackerTerritory);
//        when(player.getTerritoryByName("DefenderTerritory")).thenReturn(defenderTerritory);
//        when(attackerTerritory.getArmies()).thenReturn(10);
//        when(defenderTerritory.getArmies()).thenReturn(1);
//
//        turn.setNumAttDice(3);
//        turn.setNumDefDice(4);
//
//        turn.attack();
//
//        assertTrue(turn.isConquered());
//        verify(game, atLeastOnce()).broadcast(eq(game.getId()), eq(player.getUserName()), any(ResultNoticeBody.class));
//    }

//    @Test
//    void moveTroops_ShouldCorrectlyMoveTroopsAfterConquest() throws GameException, DatabaseConnectionException, UserException {
//        when(player.getTerritoryByName("AttackerTerritory")).thenReturn(attackerTerritory);
//        when(player.getTerritoryByName("DefenderTerritory")).thenReturn(defenderTerritory);
//        when(attackerTerritory.getArmies()).thenReturn(10);
//        when(defenderTerritory.getArmies()).thenReturn(5);
//
//        turn.moveTroops(3);
//
//        verify(attackerTerritory).setArmies(7);
//        verify(defenderTerritory).setArmies(8);
//    }
    
    @Test
    void ShouldReturnTrue_IfPlayerOwnsTerritory() {
		List<Territory> territories = Arrays.asList(Territory.builder().name("Territory1").build(), Territory.builder().name("Territory2").build());
    	Continent continent = Continent.builder().name("continent").territories(territories).bonusArmies(5).build();
    	
    	List<String> territoriesName = territories.stream().map(Territory::getName).collect(Collectors.toList());
    	List<String> continentName = continent.getTerritories().stream().map(Territory::getName).collect(Collectors.toList());
        
        for (Territory territory : territories) {
        	assertTrue(continentName.contains(territory.getName()));
        }
        
        int troops = 0;

        if (territoriesName.containsAll(continentName))
            troops = continent.getBonusArmies();
        
        assertEquals(5, troops);

    }
}
