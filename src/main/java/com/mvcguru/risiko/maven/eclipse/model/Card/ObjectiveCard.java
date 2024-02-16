package com.mvcguru.risiko.maven.eclipse.model.Card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.objective.Objective;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ObjectiveCard extends ICard{
	
	@JsonProperty("objective")
	private Objective objective;
	
	public boolean isComplete(IGame game) {
		return objective.isComplete(game);
	}
}
