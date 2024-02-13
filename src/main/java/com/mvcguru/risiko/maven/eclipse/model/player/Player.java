package com.mvcguru.risiko.maven.eclipse.model.player;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.Card.ICard;
import com.mvcguru.risiko.maven.eclipse.model.Card.ObjectiveCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Player {	
	
	private String userName;
	private String gameId;
	private String color;
	
	@JsonIgnore
	private IGame game;
	
	private PlayerColor color;
	
	private List<Territory> territories;
	
	private List<ICard> comboCards;
	
	private ICard objective;
	
	public enum PlayerColor {
		RED, YELLOW, GREEN, BLUE, BLACK, PURPLE
	}
}


