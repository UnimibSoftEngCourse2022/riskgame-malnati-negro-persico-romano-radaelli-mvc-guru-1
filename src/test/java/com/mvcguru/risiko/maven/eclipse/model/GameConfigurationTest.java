package com.mvcguru.risiko.maven.eclipse.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.model.GameConfiguration.GameMode;

class GameConfigurationTest {
    private GameConfiguration gameConfiguration;

    @BeforeEach
    void setUp() {
        gameConfiguration = GameConfiguration.builder()
                .mode(GameMode.EASY)
                .numberOfPlayers(2)
                .idMap("mappa1")
                .build();
    }
    
    
    @Test
    void testNoArgsConstructor() {
        GameConfiguration config = new GameConfiguration();
        assertNotNull(config, "GameConfiguration should be instantiated using no-args constructor");
    }

    @Test
    void testGettersAndSetters() {
        GameConfiguration config = new GameConfiguration();
        config.setMode(GameMode.EASY);
        config.setNumberOfPlayers(4);
        config.setIdMap("Map1");

        assertEquals(GameMode.EASY, config.getMode(), "Mode getter should return what was set");
        assertEquals(4, config.getNumberOfPlayers(), "NumberOfPlayers getter should return what was set");
        assertEquals("Map1", config.getIdMap(), "IdMap getter should return what was set");
    }

    @Test
    void testBuilder() {
        GameConfiguration config = GameConfiguration.builder()
                                                    .mode(GameMode.HARD)
                                                    .numberOfPlayers(2)
                                                    .idMap("Map2")
                                                    .build();

        assertEquals(GameMode.HARD, config.getMode(), "Builder should set mode correctly");
        assertEquals(2, config.getNumberOfPlayers(), "Builder should set numberOfPlayers correctly");
        assertEquals("Map2", config.getIdMap(), "Builder should set idMap correctly");
    }

    @Test
    void serializationAndDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        GameConfiguration originalConfig = GameConfiguration.builder()
                                                            .mode(GameMode.MEDIUM)
                                                            .numberOfPlayers(3)
                                                            .idMap("Map3")
                                                            .build();

        String json = mapper.writeValueAsString(originalConfig);
        assertNotNull(json, "Serialized JSON should not be null");

        GameConfiguration deserializedConfig = mapper.readValue(json, GameConfiguration.class);
        assertNotNull(deserializedConfig, "Deserialized GameConfiguration should not be null");
        assertEquals(originalConfig.getMode(), deserializedConfig.getMode(), "Mode should match after deserialization");
        assertEquals(originalConfig.getNumberOfPlayers(), deserializedConfig.getNumberOfPlayers(), "NumberOfPlayers should match after deserialization");
        assertEquals(originalConfig.getIdMap(), deserializedConfig.getIdMap(), "IdMap should match after deserialization");
    }
    
    
    
    

    @Test
    void testGameConfiguration() {
        assertEquals(GameMode.EASY.toString(), gameConfiguration.getMode().toString());
        
        assertEquals(2, gameConfiguration.getNumberOfPlayers());
        assertEquals("mappa1", gameConfiguration.getIdMap());
    }
}
