package com.mvcguru.risiko.maven.eclipse.actions;

import java.io.IOException;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.AttackRequestBody;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.states.GameState;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class AttackRequest extends ActionPlayer{

	private AttackRequestBody requestAttackBody;
	@Override
	public void accept(GameState gameState) throws FullGameException, IOException {
		gameState.onActionPlayer(this);
	}

}
