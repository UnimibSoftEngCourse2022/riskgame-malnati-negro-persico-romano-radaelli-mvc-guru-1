package com.mvcguru.risiko.maven.eclipse.model;

import java.util.List;
import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Territory {
	
	private int continent;
	
	private String name;
	
	private int armies;
	
	private Player owner;
	
	private List<Territory> neighbors;
	
	private String svgPath;
}
