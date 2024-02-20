package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class AttackRequestBody implements Serializable{
	
	private BattleBody attackerTerritory;
	
	private BattleBody defenderTerritory;
	
	private int numAttDice;
}
