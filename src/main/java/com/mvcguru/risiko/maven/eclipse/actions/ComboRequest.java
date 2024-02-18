package com.mvcguru.risiko.maven.eclipse.actions;

import java.io.IOException;

import com.mvcguru.risiko.maven.eclipse.controller.body_request.ComboRequestBody;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ComboRequest extends ActionPlayer{

	private ComboRequestBody comboRequestBody;
	
	@Override
	public void accept(GameState gameState) throws FullGameException, IOException {
		gameState.onActionPlayer(this);
		
	}

}
