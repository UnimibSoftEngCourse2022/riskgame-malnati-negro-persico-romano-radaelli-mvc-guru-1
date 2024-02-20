package com.mvcguru.risiko.maven.eclipse.states;

import com.mvcguru.risiko.maven.eclipse.actions.EndTurn;
import com.mvcguru.risiko.maven.eclipse.actions.EndTurnMovement;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class EndTurnState extends GameState {
	
	@Override
	public void onActionPlayer(EndTurnMovement endTurnMovement) throws GameException, DatabaseConnectionException, UserException { 
		Territory startTerritory = endTurnMovement.getPlayer()
				.getTerritoryByName(endTurnMovement.getEndTurnMovementBody().getNameTerritoryStart());
		Territory endTerritory = endTurnMovement.getPlayer()
				.getTerritoryByName(endTurnMovement.getEndTurnMovementBody().getNameTerritoryEnd());
		startTerritory.setArmies(startTerritory.getArmies() - endTurnMovement.getEndTurnMovementBody().getNumTroops());
		endTerritory.setArmies(endTerritory.getArmies() + endTurnMovement.getEndTurnMovementBody().getNumTroops());
		
		GameRepository.getInstance().updateTerritoryArmies(
				startTerritory.getName(), endTurnMovement.getPlayer().getGameId(), startTerritory.getArmies());
		GameRepository.getInstance().updateTerritoryArmies(
				endTerritory.getName(), endTurnMovement.getPlayer().getGameId(), endTerritory.getArmies());
	}
	
	@Override
	public void onActionPlayer(EndTurn endTurn) throws GameException, DatabaseConnectionException, UserException { 
		//if(endTurn.getPlayer().getObjective()){
		//game.setState(EndGameState.builder().game(game).build());
		//setWinner(endTurn.getPlayer());
		//game.
		if(game.getCurrentTurn().isConquered())
			endTurn.getPlayer().getComboCards().add((TerritoryCard)game.getDeckTerritory().drawCard());
		game.changeTurn();
		game.setState(StartTurnState.builder().game(game).build());
		GameRepository.getInstance().updateState(game.getId(), game.getState());
	}
}
