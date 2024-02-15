package com.mvcguru.risiko.maven.eclipse.model.Card;

import com.mvcguru.risiko.maven.eclipse.model.Territory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TerritoryCard extends ICard{
	
	private Territory territory;
	private CardSymbol symbol;		//attributo per il simbolo della carta
	
	public enum CardSymbol {
		INFANTRY,
		CAVALRY,
		ARTILLERY;
	}
	
	public TerritoryCard(Territory territory) {
		this.territory = territory;
		this.symbol = CardSymbol.values()[(int) (Math.random() * CardSymbol.values().length)];
		
		
	}
	
}
