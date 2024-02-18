package com.mvcguru.risiko.maven.eclipse.controller.bodyRequest;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.Territory;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class SetUpBody {
	
	private String username;
	
	private List<TerritoryBody> territories;
}
