package com.mvcguru.risiko.maven.eclipse.controller.bodyRequest;

import java.util.List;

import com.mvcguru.risiko.maven.eclipse.model.card.ICard;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ComboRequestBody {
	
	private String username;
	
	private List<ICard> comboCards;

}
