package com.mvcguru.risiko.maven.eclipse.model.player;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable{	
	
	private String userName;
	
	private String gameId;
	
	private PlayerColor color;
	
	@JsonIgnore
	private IGame game;
	
	private List<Territory> territories;
	
	private List<TerritoryCard> comboCards;
	
	private boolean setUpCompleted = false;
	
	private ICard objective;

	public enum PlayerColor {
		RED, YELLOW, GREEN, BLUE, BLACK, PURPLE, GREY
	}

}
