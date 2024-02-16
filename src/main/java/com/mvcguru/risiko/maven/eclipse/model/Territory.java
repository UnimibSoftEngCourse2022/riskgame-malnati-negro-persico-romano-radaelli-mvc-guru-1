package com.mvcguru.risiko.maven.eclipse.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard.CardSymbol;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Territory {
	
	private int continent;
	
	private String name;
	
	private int armies;
	
	private Player owner;
	
	private List<Territory> neighbors;
}
