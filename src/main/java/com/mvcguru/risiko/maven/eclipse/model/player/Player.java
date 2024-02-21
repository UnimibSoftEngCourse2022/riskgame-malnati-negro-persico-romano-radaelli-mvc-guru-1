package com.mvcguru.risiko.maven.eclipse.model.player;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable{
	Logger LOGGER = LoggerFactory.getLogger(Player.class);
	
	private String userName;
	
	private String gameId;
	
	private PlayerColor color;
	
	@JsonIgnore
	private IGame game;
	
	private List<Territory> territories;
	
	private List<TerritoryCard> comboCards;
	
	private boolean setUpCompleted = false;
	
	private ObjectiveCard objective;

	public enum PlayerColor {
		RED, YELLOW, GREEN, BLUE, BLACK, PURPLE, GREY
	}
	
	public Territory getTerritoryByName(String name) {
		try {
			return GameRepository.getInstance().getTerritory(userName, gameId, name);
		} catch (GameException | DatabaseConnectionException | UserException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

}
