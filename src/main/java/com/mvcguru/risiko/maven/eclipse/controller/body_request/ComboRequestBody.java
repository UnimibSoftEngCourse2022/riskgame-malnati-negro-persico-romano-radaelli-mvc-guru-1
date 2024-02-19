package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ComboRequestBody implements Serializable{
	
	private String username;
	
	private List<TerritoryCardBody> comboCards;

}
