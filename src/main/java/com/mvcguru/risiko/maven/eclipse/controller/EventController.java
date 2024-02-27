package com.mvcguru.risiko.maven.eclipse.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.mvcguru.risiko.maven.eclipse.actions.AttackRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.ConquerAssignment;
import com.mvcguru.risiko.maven.eclipse.actions.DefenceRequest;
import com.mvcguru.risiko.maven.eclipse.actions.EndTurn;
import com.mvcguru.risiko.maven.eclipse.actions.EndTurnMovement;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.GameExit;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.actions.TurnSetUp;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.AttackRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.ComboRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.DefenceRequestBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.EndTurnMovementBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.PlayerBody;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.SetUpBody;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.Territory;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

@Controller
public class EventController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

	@Autowired
    public EventController(SimpMessagingTemplate template) {
        MessageBrokerSingleton.setInstance(MessageBrokerSingleton.builder()
                .template(template)
                .build());
    }
	
	@MessageMapping("/partite/{id}/entra")
    public void enterInTheGame(@Payload PlayerBody body, @DestinationVariable String id) throws Exception {
		IGame game = null;
		try {
			game = GameRepository.getInstance().getCompletedGame(id);
			Player player = Player.builder().userName(body.getUsername()).gameId(id).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
			GameEntry action = GameEntry.builder().player(player).build();
			GameRepository.getInstance().addPlayer(player);
			game.onActionPlayer(action);
			
		} catch (GameException | DatabaseConnectionException | UserException e) {throw e;
		} catch (FullGameException e) {
            MessageBrokerSingleton.getInstance().getTemplate().convertAndSend("/topic/partite/" + id, "Partita piena");
        }
    }
	
	@MessageMapping("/partite/{id}/esci") 
    public void exit(
            @DestinationVariable String id,
            @Payload PlayerBody body) throws Exception {
		IGame game = null;
		try {
            game = GameRepository.getInstance().getCompletedGame(id);
            Player player = game.findPlayerByUsername(body.getUsername());
            GameExit action = GameExit.builder().player(player).build();
            GameRepository.getInstance().removePlayer(body.getUsername());
            game.onActionPlayer(action);
        } catch (GameException | DatabaseConnectionException | UserException e) {LOGGER.error("Errore durante l'uscita dalla partita", e);}    }
	
	@MessageMapping("/partite/{id}/confermaSetup")
	public void confirmSetup(@DestinationVariable String id, @Payload SetUpBody body) throws Exception {
	    try {	    
	    	LOGGER.info("Inizio setup");
	        IGame game = GameRepository.getInstance().getCompletedGame(id);
	        Player player = game.findPlayerByUsername(body.getUsername());
	        if (player != null) {
	            TerritorySetup action = TerritorySetup.builder().player(player).setUpBody(body).build();
	            
	            game.onActionPlayer(action);
	        }
	        LOGGER.info("Fine setup player : {}", game.findPlayerByUsername(body.getUsername()));
	        LOGGER.info("Fine setup territori: {}", game.findPlayerByUsername(body.getUsername()).getTerritories());
	    } catch (Exception e) {LOGGER.error("Errore durante la conferma del setup", e);}
	}
	


	public void comboRequest(@DestinationVariable String id, @Payload ComboRequestBody body) throws Exception {
		try {
			LOGGER.info("Inizio Combo request");
			IGame game = GameRepository.getInstance().getCompletedGame(id);
			Player player = game.findPlayerByUsername(body.getUsername());
			if (player != null) {
				ComboRequest action = ComboRequest.builder().player(player).comboRequestBody(body).build();
				game.onActionPlayer(action);
			}
			LOGGER.info("Fine Combo request");
		}catch(Exception e) {LOGGER.error("Errore durante la combo request", e);}
	}
	
	@MessageMapping("/partite/{id}/turnAssignation")
	public void turnAssignation(@DestinationVariable String id, @Payload SetUpBody body) throws Exception {
		try {
			IGame game = GameRepository.getInstance().getCompletedGame(id);
			Player player = game.findPlayerByUsername(body.getUsername());
			if (player != null) {
				TurnSetUp action = TurnSetUp.builder().player(player).setUpBody(body).build();
				LOGGER.info("Parte action");
				game.onActionPlayer(action);
				LOGGER.info("Fine assegnazione del turno");
			}
		} catch (Exception e) { LOGGER.error("Errore durante l'assegnazione del turno", e); }
	}
	
	@MessageMapping("/partite/{id}/attack")
	public void attackRequest(@DestinationVariable String id, @Payload AttackRequestBody body) {
		LOGGER.info("1------   Inizio attacco {}", body);
		try {
			IGame game = GameRepository.getInstance().getCompletedGame(id);
			Player player = game.findPlayerByUsername(body.getAttackerTerritory().getUsername());
			if(player != null) {
				AttackRequest action = AttackRequest.builder().player(player).requestAttackBody(body).build();
				game.onActionPlayer(action);
			}
		}
		catch(Exception e){ LOGGER.error("Errore durante la richiesta di attacco", e);}
	}
	
	@MessageMapping("/partite/{id}/defence")
	public void defenceRequest(@DestinationVariable String id, @Payload DefenceRequestBody body) {
		try {
			LOGGER.info("Inizio difesa {}", body);
			IGame game = GameRepository.getInstance().getCompletedGame(id);
			Player player = game.findPlayerByUsername(body.getUsername());
			if(player != null) {
				DefenceRequest action = DefenceRequest.builder().player(player).defenderRequestBody(body).build();
				game.onActionPlayer(action);
			}
		}
		catch(Exception e){ LOGGER.error("Errore durante la difesa", e);}
	}
	
	@MessageMapping("/partite/{id}/conquerAssigment")
	public void conquerAssignment(@DestinationVariable String id, @Payload int troops) {
		try {
			IGame game = GameRepository.getInstance().getCompletedGame(id);
			game.getCurrentTurn().moveTroops(troops);
			Player player = game.findPlayerByUsername(game.getCurrentTurn().getPlayer().getUserName());
			if(player != null) {
				ConquerAssignment action = ConquerAssignment.builder().player(player).numTroops(troops).build();
				game.onActionPlayer(action);
			}
			
		}
		catch(Exception e){ LOGGER.error("Errore durante la richiesta conquerAssigment", e);}
	}

	@MessageMapping("/partite/{id}/endTurnMovement")
	public void endTurnMovement(@DestinationVariable String id, @Payload EndTurnMovementBody body) {
		try {
			IGame game = GameRepository.getInstance().getCompletedGame(id);
			Player player = game.findPlayerByUsername(body.getUsername());
			if(player != null) {
				EndTurnMovement action = EndTurnMovement.builder().player(player).endTurnMovementBody(body).build();
				game.onActionPlayer(action);
			}
		}
		catch(Exception e){ LOGGER.error("Errore durante la richiesta endTurnMovement", e);}
	}
	
	@MessageMapping("/partite/{id}/endTurn")
	public void endTurn(@DestinationVariable String id, @Payload String username ) {
		try {
			IGame game = GameRepository.getInstance().getCompletedGame(id);
			Player player = game.findPlayerByUsername(username);
			EndTurn action = EndTurn.builder().player(player).build();
			game.onActionPlayer(action);
		}
		catch(Exception e){ LOGGER.error("Errore in endTurn", e);}
	}
	
}
