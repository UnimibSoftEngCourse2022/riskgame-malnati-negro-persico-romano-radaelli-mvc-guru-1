package com.mvcguru.risiko.maven.eclipse.model.objective;

import com.mvcguru.risiko.maven.eclipse.model.IGame;

public class TerritoriesObjective extends Objective{

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public boolean isComplete(IGame game) {
		return false;
	}

}
