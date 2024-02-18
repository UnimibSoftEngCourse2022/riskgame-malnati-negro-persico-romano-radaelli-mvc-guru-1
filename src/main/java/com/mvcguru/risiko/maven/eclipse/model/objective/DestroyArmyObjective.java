package com.mvcguru.risiko.maven.eclipse.model.objective;

import com.mvcguru.risiko.maven.eclipse.model.IGame;

public class DestroyArmyObjective extends Objective{

	@Override
	public boolean isComplete(IGame game) {
		return false;
	}

}
