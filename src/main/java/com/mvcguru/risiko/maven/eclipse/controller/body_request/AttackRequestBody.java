package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import java.io.Serializable;

import com.mvcguru.risiko.maven.eclipse.model.Territory;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AttackRequestBody implements Serializable{
	
	private Territory attackerTerritory;
	
	private Territory defenderTerritory;
	
	private int numDice;
	
	private String idGame;
}
