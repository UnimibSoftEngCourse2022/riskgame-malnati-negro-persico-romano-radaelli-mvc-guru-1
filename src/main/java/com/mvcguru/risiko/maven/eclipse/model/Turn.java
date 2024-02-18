package com.mvcguru.risiko.maven.eclipse.model;

import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvcguru.risiko.maven.eclipse.controller.body_request.AttackRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.DefenderNoticeBody;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Turn implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Turn.class);

    private Player player;
    private int numberOfTroops;
    private List<TerritoryCard> comboCards;
    private List<Continent> continent;

    public void numberOfTroopsCalculation(List<Territory> territories) throws IOException {
        numberOfTroops = territories.size() / 3;
        numberOfTroops += comboCardsCheck();
        numberOfTroops += continentCheck(territories);
    }

    public int continentCheck(List<Territory> territories) throws IOException {
        int troops = 0;
        continent = player.getGame().parsingContinent();
        List<String> territoriesName = territories.stream().map(Territory::getName).collect(Collectors.toList());

        for (Continent c : continent) {
            List<String> continentName = c.getTerritories().stream().map(Territory::getName).collect(Collectors.toList());
            if (territoriesName.containsAll(continentName)) {
                troops += c.getBonusArmies();
            } else {
                LOGGER.info("Player doesn't have all the territories of the continent");
            }
        }
        return troops;
    }

    public int comboCardsCheck() {
        if (comboCards.size() != 3) {
            return 0;
        }

        long distinctSymbols = comboCards.stream().map(TerritoryCard::getSymbol).distinct().count();
        if (distinctSymbols == 1) {
            return troopsForSingleSymbolCombo();
        } else if (distinctSymbols == 3 || comboCards.stream().anyMatch(card -> card.getSymbol() == CardSymbol.JOLLY)) {
            return troopsForMixedCombo();
        }
        return 0;
    }

    private int troopsForSingleSymbolCombo() {
        switch (comboCards.get(0).getSymbol()) {
            case ARTILLERY:
                return 4;
            case CAVALRY:
                return 6;
            case INFANTRY:
                return 8;
            default:
                return 0;
        }
    }

    private int troopsForMixedCombo() {
        int troops = comboCards.stream().anyMatch(card -> card.getSymbol() == CardSymbol.JOLLY) ? 12 : 10;
        for (TerritoryCard card : comboCards) {
            if (player.getTerritories().contains(card.getTerritory()))
                troops += 2;
        }
        return troops;
    }

    public void attack(AttackRequestBody attackRequestBody, DefenderNoticeBody defenderNoticeBody) {
        int numAttDice = attackRequestBody.getNumDice();
        int numDefDice = defenderNoticeBody.getNumAttDice();

        Integer[] attRolls = rollDice(numAttDice);
        Integer[] defRolls = rollDice(numDefDice);

        Arrays.sort(attRolls, Collections.reverseOrder());
        Arrays.sort(defRolls, Collections.reverseOrder());

        calculateLosses(attRolls, defRolls);
    }

    private Integer[] rollDice(int numDice) {
        SecureRandom random = new SecureRandom();
        return random.ints(numDice, 1, 7).boxed().toArray(Integer[]::new);
    }

    private void calculateLosses(Integer[] attRolls, Integer[] defRolls) {
        int numComparisons = Math.min(attRolls.length, defRolls.length);
        for (int i = 0; i < numComparisons; i++) {
            if (attRolls[i] > defRolls[i]) {
                LOGGER.info("Defender loses a unit");
            } else {
                LOGGER.info("Attacker loses a unit");
            }
        }
    }
}
