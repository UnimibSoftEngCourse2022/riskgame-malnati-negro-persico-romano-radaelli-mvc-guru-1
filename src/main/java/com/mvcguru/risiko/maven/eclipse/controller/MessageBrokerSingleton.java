package com.mvcguru.risiko.maven.eclipse.controller;

import lombok.Builder;
import lombok.Data;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.mvcguru.risiko.maven.eclipse.model.IGame;

@Data
@Builder
public class MessageBrokerSingleton {
    private static MessageBrokerSingleton instance;
    private final SimpMessagingTemplate template;

    public static MessageBrokerSingleton getInstance() {
        return instance;
    }

    public static void setInstance(MessageBrokerSingleton instance) {
        MessageBrokerSingleton.instance = instance;
    }

    public synchronized void broadcast(IGame game) {
        template.convertAndSend("/topic/partite/" + game.getId(), game);
    }


    public <T> void broadcast(String idGame, String idUser, T object){
        template.convertAndSend("/topic/partite/" + idGame + "/" + idUser, object);
    }
}

