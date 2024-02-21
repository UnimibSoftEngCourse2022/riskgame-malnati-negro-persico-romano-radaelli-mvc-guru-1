package com.mvcguru.risiko.maven.eclipse.model.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.card.objectives.ConquerContinentObjective;
import com.mvcguru.risiko.maven.eclipse.model.card.objectives.DestroyArmyObjective;
import com.mvcguru.risiko.maven.eclipse.model.card.objectives.TerritoriesObjective;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConquerContinentObjective.class, name = "conquerContinent"),
    @JsonSubTypes.Type(value = DestroyArmyObjective.class, name = "destroyArmy"),
    @JsonSubTypes.Type(value = TerritoriesObjective.class, name = "territories")
})
public abstract class ObjectiveCard extends ICard{
	
	@JsonProperty("objective")
	private String objective;
	
	public abstract boolean isComplete(IGame game, String username);
}
