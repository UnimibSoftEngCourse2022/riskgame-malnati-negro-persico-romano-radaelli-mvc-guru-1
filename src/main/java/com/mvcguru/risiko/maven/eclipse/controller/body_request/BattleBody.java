package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BattleBody implements Serializable{

	private String nameTerritory;
	
	private String username;
}
