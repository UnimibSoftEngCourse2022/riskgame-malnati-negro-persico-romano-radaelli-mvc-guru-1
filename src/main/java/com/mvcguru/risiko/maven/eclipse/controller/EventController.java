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

import com.mvcguru.risiko.maven.eclipse.actions.ComboRequest;
import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.GameExit;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.actions.TurnSetUp;
import com.mvcguru.risiko.maven.eclipse.controller.body_request.ComboRequestBody;
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
			game = GameRepository.getInstance().getGameById(id);
			LOGGER.info("Partita trovata: {}", game.getId());
			Player player = Player.builder().userName(body.getUsername()).gameId(id).territories(new ArrayList<Territory>()).color(Player.PlayerColor.GREY).build();
			GameEntry action = GameEntry.builder().player(player).build();
			game.onActionPlayer(action);
			LOGGER.info("Partita dopo action: {}", game);
			GameRepository.getInstance().addPlayer(player);
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
            game = GameRepository.getInstance().getGameById(id);
            Player player = game.findPlayerByUsername(body.getUsername());
            GameExit action = GameExit.builder().player(player).build();
            game.onActionPlayer(action);
            GameRepository.getInstance().removePlayer(body.getUsername());
        } catch (GameException | DatabaseConnectionException | UserException e) {LOGGER.error("Errore durante l'uscita dalla partita", e);}    }
	
	@MessageMapping("/partite/{id}/confermaSetup")
	public void confirmSetup(@DestinationVariable String id, @Payload SetUpBody body) throws Exception {
	    try {	    
	    	LOGGER.info("Inizio setup");
	        IGame game = GameRepository.getInstance().getGameById(id);
	        
	        Player player = game.findPlayerByUsername(body.getUsername());
	        if (player != null) {
	            TerritorySetup action = TerritorySetup.builder().player(player).setUpBody(body).build();
	            game.onActionPlayer(action);
	        }
	        LOGGER.info("Fine setup player : {}", game.findPlayerByUsername(body.getUsername()));
	        LOGGER.info("Fine setup territori: {}", game.findPlayerByUsername(body.getUsername()).getTerritories());
	    } catch (Exception e) {LOGGER.error("Errore durante la conferma del setup", e);}
	}
	
	@MessageMapping("/partite/{id}/comboRequest")
	public void comboRequest(@DestinationVariable String id, @Payload ComboRequestBody body) throws Exception {
		try {
			LOGGER.info("Inizio Combo request");
			IGame game = GameRepository.getInstance().getGameById(id);
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
			IGame game = GameRepository.getInstance().getGameById(id);
			Player player = game.findPlayerByUsername(body.getUsername());
			if (player != null) {
				TurnSetUp action = TurnSetUp.builder().player(player).setUpBody(body).build();
				game.onActionPlayer(action);
			}
		} catch (Exception e) {
			LOGGER.error("Errore durante l'assegnazione del turno", e);
		}
	}
	
	


}
