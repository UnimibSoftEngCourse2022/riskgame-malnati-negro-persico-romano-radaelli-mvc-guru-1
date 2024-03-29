package com.mvcguru.risiko.maven.eclipse.model.card;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TerritoryCard extends ICard implements Serializable{
	
	@JsonProperty("territory")
	private Territory territory;
	
	@JsonProperty("symbol")
	private CardSymbol symbol;
	
	public enum CardSymbol {
		INFANTRY,
		CAVALRY,
		ARTILLERY,
		JOLLY;
	}
	
}
