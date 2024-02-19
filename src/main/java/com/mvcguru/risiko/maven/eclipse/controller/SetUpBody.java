package com.mvcguru.risiko.maven.eclipse.controller;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.controller.body_request.TerritoryBody;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SetUpBody {
	private String username;
	
	private List<TerritoryBody> territories;
}