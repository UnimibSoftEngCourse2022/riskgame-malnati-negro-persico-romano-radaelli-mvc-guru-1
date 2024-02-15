package com.mvcguru.risiko.maven.eclipse.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Territory {
	
	@JsonProperty("continent")
	private int continent;
	
	@JsonProperty("name")
	private String name;
	
	private int armies;
	
	private Player owner;
	
	private List<Territory> neighbors;
	
	

}
