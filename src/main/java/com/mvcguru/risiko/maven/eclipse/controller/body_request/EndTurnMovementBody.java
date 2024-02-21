package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class EndTurnMovementBody implements Serializable{

	String nameTerritoryStart;
	
	String nameTerritoryEnd;
	
	int numTroops;
	
	String username;
}
