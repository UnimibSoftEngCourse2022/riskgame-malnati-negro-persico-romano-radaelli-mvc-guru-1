package com.mvcguru.risiko.maven.eclipse.model.Card;

import com.mvcguru.risiko.maven.eclipse.states.GameSetupState;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
public class ObjectiveCard extends ICard{
	
	private String objective;


}
