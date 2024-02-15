package com.mvcguru.risiko.maven.eclipse.model.Card;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ObjectiveCard extends ICard{
	@JsonProperty("objective")
	private String objective;
	

}
