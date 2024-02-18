package com.mvcguru.risiko.maven.eclipse.controller.bodyRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TerritoryBody {
	
	private String name;
	private int troops;

}
