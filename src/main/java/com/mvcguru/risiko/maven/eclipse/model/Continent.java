package com.mvcguru.risiko.maven.eclipse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@SuperBuilder
public class Continent {

    @JsonProperty("continentId")
    private int continentId;

    @JsonProperty("continentName")
    private String continentName;

    @JsonProperty("territories")
    private List<Territory> territories;

    


}
