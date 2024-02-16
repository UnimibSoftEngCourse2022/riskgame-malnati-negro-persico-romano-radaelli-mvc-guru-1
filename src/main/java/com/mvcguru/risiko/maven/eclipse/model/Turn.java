package com.mvcguru.risiko.maven.eclipse.model;

import java.io.Serializable;

import com.mvcguru.risiko.maven.eclipse.model.player.Player;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Turn implements Serializable{

	private Player player;
	
}
