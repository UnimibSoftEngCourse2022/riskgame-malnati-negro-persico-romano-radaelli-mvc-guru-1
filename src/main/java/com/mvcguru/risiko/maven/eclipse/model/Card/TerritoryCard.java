package com.mvcguru.risiko.maven.eclipse.model.Card;

import com.mvcguru.risiko.maven.eclipse.model.Territory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TerritoryCard extends ICard{
	
	Territory territory;
	
}
