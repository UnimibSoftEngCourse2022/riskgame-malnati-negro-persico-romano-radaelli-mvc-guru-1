package com.mvcguru.risiko.maven.eclipse.controller.bodyRequest;

import com.mvcguru.risiko.maven.eclipse.model.Territory;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AttackRequestBody {
	
	private Territory attackerTerritory;
	
	private Territory defenderTerritory;
	
	private int numDice;
	
	private String idGame;
}
