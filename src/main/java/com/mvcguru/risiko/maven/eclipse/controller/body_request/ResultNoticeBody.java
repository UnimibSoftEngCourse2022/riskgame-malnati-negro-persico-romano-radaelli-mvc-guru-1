package com.mvcguru.risiko.maven.eclipse.controller.body_request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ResultNoticeBody {

	private boolean isConquered;
	
	private int lostAttTroops;
	
	private int lostDefTroops;
	
}
