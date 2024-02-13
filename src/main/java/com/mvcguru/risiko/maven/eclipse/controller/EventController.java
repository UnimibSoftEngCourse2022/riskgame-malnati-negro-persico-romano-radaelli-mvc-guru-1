package com.mvcguru.risiko.maven.eclipse.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.mvcguru.risiko.maven.eclipse.actions.GameEntry;
import com.mvcguru.risiko.maven.eclipse.exception.DatabaseConnectionException;
import com.mvcguru.risiko.maven.eclipse.exception.FullGameException;
import com.mvcguru.risiko.maven.eclipse.exception.GameException;
import com.mvcguru.risiko.maven.eclipse.exception.UserException;
import com.mvcguru.risiko.maven.eclipse.model.IGame;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;
import com.mvcguru.risiko.maven.eclipse.service.GameRepository;

@Controller
public class EventController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

	@Autowired
    public EventController(SimpMessagingTemplate template) {
        MessageBrokerSingleton.setInstance(MessageBrokerSingleton.builder()
                .template(template)
                .build());
    }
	
	@MessageMapping("/partite/{id}/entra")
    public void enterInTheGame(@Payload PlayerBody body, @DestinationVariable String id) {
		IGame game = null;
		try {
			game = GameRepository.getInstance().getGameById(id);
			Player player = Player.builder().userName(body.getUsername()).gameId(id).build();
			GameEntry action = GameEntry.builder().player(player).build();
			game.onActionPlayer(action);
			GameRepository.getInstance().add(player);
		} catch (GameException | DatabaseConnectionException | UserException e) {
			//segnala errore
		} catch (FullGameException e) {
			Map<String, Object> headers = new HashMap<>();
            headers.put("nickname", body.getUsername());
            MessageBrokerSingleton.getInstance().getTemplate().convertAndSend("/topic/partite/" + id, "Partita piena", headers);
        }
    }
	
	@MessageMapping("/partite/{id}/esci")
    public void esci(
            @DestinationVariable String id,
            @Payload PlayerBody body) throws FullGameException {
		IGame game = null;
		try {
            game = GameRepository.getInstance().getGameById(id);
            Player player = Player.builder().userName(body.getUsername()).game(game).build();
            game.getPlayers().remove(player);
            GameRepository.getInstance().remove(body.getUsername());
        } catch (GameException | DatabaseConnectionException | UserException e) {
            //segnala errore
        }
    }

}
