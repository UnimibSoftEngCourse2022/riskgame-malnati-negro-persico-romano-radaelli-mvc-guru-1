package com.mvcguru.risiko.maven.eclipse.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvcguru.risiko.maven.eclipse.model.IGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Player {	
	
	private String userName;
	private String gameId;
	private String color;
	
	@JsonIgnore
	private IGame game;
}


