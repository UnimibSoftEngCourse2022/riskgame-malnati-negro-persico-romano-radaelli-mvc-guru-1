package com.mvcguru.risiko.maven.eclipse.model.objective;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvcguru.risiko.maven.eclipse.model.card.ObjectiveCard;

public class ObjectiveTest {

	//Objective objective;
//	
//	@Test
//	void testParsing() throws IOException {
//		ObjectMapper mapper = new ObjectMapper();
//        byte[] data = FileCopyUtils.copyToByteArray(new ClassPathResource("objectives_hard.json").getInputStream());
//        String json = new String(data, StandardCharsets.UTF_8);
//        Objective[] objectives = mapper.readValue(json, Objective[].class);
//        
//        assertEquals(objectives.length, 15);
//        
//        System.out.println("-----------------------------------------------");
//        System.out.println("Objectives Card" + objectives.toString());
//        System.out.println("-----------------------------------------------");
//	}
}
