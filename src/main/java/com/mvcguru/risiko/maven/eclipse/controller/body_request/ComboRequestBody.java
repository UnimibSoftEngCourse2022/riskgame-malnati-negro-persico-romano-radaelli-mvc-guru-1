package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import java.io.Serializable;
import java.util.List;
import com.mvcguru.risiko.maven.eclipse.model.card.TerritoryCard;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ComboRequestBody implements Serializable{
	
	private String username;
	
	private List<TerritoryCard> comboCards;

}
