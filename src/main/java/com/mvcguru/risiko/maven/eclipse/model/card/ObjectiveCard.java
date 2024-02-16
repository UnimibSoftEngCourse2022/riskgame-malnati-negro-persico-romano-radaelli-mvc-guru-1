package com.mvcguru.risiko.maven.eclipse.model.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvcguru.risiko.maven.eclipse.model.card.ICard;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ObjectiveCard extends ICard{
	@JsonProperty("objective")
	private String objective;
	

}
