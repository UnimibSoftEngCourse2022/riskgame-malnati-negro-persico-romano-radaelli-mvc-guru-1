package com.mvcguru.risiko.maven.eclipse.controller;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TerritoryBody {
	
	private String name;
	private int troops;

}
