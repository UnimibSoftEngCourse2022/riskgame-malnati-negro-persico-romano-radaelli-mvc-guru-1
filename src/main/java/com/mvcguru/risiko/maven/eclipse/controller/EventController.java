package com.mvcguru.risiko.maven.eclipse.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.actions.TerritorySetup;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

@Controller
public class EventController {

	@Autowired
    public EventController(SimpMessagingTemplate template) {
        MessageBrokerSingleton.setInstance(MessageBrokerSingleton.builder()
                .template(template)
                .build());
    }
	
	@MessageMapping("/partite/{id}/entra")
    public void enterInTheGame(@Payload PlayerBody body, @DestinationVariable String id) throws IOException {
		IGame game = null;
		try {
			game = GameRepository.getInstance().getGameById(id);
			Player player = Player.builder().userName(body.getUsername()).gameId(id).color(Player.PlayerColor.GREY).build();
			GameEntry action = GameEntry.builder().player(player).build();
			game.onActionPlayer(action);
			GameRepository.getInstance().addPlayer(player);
		} catch (GameException | DatabaseConnectionException | UserException e) {
			//segnala errore
		} catch (FullGameException e) {
			Map<String, Object> headers = new HashMap<>();
            headers.put("nickname", body.getUsername());
            MessageBrokerSingleton.getInstance().getTemplate().convertAndSend("/topic/partite/" + id, "Partita piena", headers);
        }
    }
	
	@MessageMapping("/partite/{id}/esci") 
    public void exit(
            @DestinationVariable String id,
            @Payload PlayerBody body) throws FullGameException, IOException {
		IGame game = null;
		try {
            game = GameRepository.getInstance().getGameById(id);
            Player player = Player.builder().userName(body.getUsername()).game(game).build();
            game.getPlayers().remove(player);
            GameRepository.getInstance().removePlayer(body.getUsername());
        } catch (GameException | DatabaseConnectionException | UserException e) {
            //segnala errore
        }
    }
	
	@MessageMapping("/partite/{id}/confermaSetup")
	public void confirmSetup(@DestinationVariable String id, @Payload SetUpBody body) {
	    try {
	        IGame game = GameRepository.getInstance().getGameById(id);
	        Player player = game.findPlayerByUsername(body.getUsername());
	        if (player != null) {
	            TerritorySetup action = TerritorySetup.builder().player(player).setUpBody(body).build();
	            game.onActionPlayer(action);
	        }
	    } catch (Exception e) {
	        //segnala errore
	    }
	}


}