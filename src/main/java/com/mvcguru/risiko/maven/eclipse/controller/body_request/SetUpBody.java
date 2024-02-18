package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class SetUpBody {
	
	private String username;
	
	private List<TerritoryBody> territories;
}
