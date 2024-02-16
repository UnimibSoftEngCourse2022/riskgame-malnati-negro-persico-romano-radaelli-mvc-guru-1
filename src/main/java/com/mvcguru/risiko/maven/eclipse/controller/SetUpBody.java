package com.mvcguru.risiko.maven.eclipse.controller;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.Territory;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SetUpBody {
	private String username;
	
	private List<Territory> territories;
}
