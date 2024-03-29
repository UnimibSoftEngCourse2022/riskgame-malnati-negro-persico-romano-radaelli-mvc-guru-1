package com.mvcguru.risiko.maven.eclipse.model;


import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Continent implements Serializable{
	
	@JsonProperty("continentId")
	private int continentId;
	
	@JsonProperty("continentName")
	private String name;
	
	@JsonProperty("territories")
	private List<Territory> territories;
	
	@JsonProperty("bonus")
	private int bonusArmies;
	
}
