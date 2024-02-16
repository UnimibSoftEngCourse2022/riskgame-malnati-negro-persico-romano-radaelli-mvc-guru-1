package com.mvcguru.risiko.maven.eclipse.model.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TerritoryCard extends ICard{
	@JsonProperty("territory")
	private Territory territory;
	
	@JsonProperty("symbol")
	private CardSymbol symbol;		//attributo per il simbolo della carta
	
	public enum CardSymbol {
		INFANTRY,
		CAVALRY,
		ARTILLERY,
		JOLLY;
	}
	
}
